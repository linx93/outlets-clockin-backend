package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.outletcn.app.common.*;
import com.outletcn.app.converter.GiftConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.mapper.GiftVoucherMapper;
import com.outletcn.app.mapper.PunchLogMapper;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.gift.GiftPunchSignatureResponse;
import com.outletcn.app.model.mongo.Destination;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mongo.GiftBagRelation;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.model.mysql.GiftVoucher;
import com.outletcn.app.model.mysql.PunchLog;
import com.outletcn.app.repository.PunchSignatureMongoRepository;
import com.outletcn.app.service.ClockInUserService;
import com.outletcn.app.service.PunchLogService;
import com.outletcn.app.service.PunchSignatureService;
import com.outletcn.app.utils.JwtUtil;
import com.outletcn.app.utils.QrcodeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author felix
 */
@Slf4j
@Service
@AllArgsConstructor
public class PunchSignatureServiceImpl implements PunchSignatureService {

    private MongoTemplate mongoTemplate;

    private GiftConverter giftConverter;

    private GiftVoucherMapper giftVoucherMapper;

    private PunchLogMapper punchLogMapper;

    private Sequence sequence;

    private PunchLogService punchLogService;

    private PunchSignatureMongoRepository punchSignatureMongoRepository;

    private ClockInUserService clockInUserService;


    /**
     * ????????????????????????
     */
    @Override
    public PageInfo<GiftPunchSignatureResponse> exchangeOrdinaryGiftList(Integer page, Integer size) {
        PageInfo<GiftBag> pageInfo = new PageInfo<>();
        pageInfo.setCurrent(page);
        pageInfo.setSize(size);
        Query query = new Query();
        Criteria criteria = Criteria.where("type").is(GiftTypeEnum.NORMAL.getCode()).and("validDate").gt(Instant.now().getEpochSecond()).and("putOn").is(0).and("sub").gt(0);
        query.addCriteria(criteria);
        PageInfo<GiftBag> bagPageInfo = punchSignatureMongoRepository.findObjForPage(query, pageInfo);
        List<GiftPunchSignatureResponse> signatureResponses = giftConverter.toGiftPunch(bagPageInfo.getRecords());
        PageInfo<GiftPunchSignatureResponse> responsePageInfo = new PageInfo<>();
        signatureResponses.forEach(item -> {
            List<Long> giftBagId = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(item.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
            List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagId)), Gift.class);
            int scoreSum = gifts.stream().mapToInt(Gift::getGiftScore).sum();
            item.setScoreSum(scoreSum);
            //??????????????????hot
            item.setHot(1);
        });
        //????????????hot??????
        //1.???????????????????????????
        List<Long> giftIds = signatureResponses.stream().map(GiftPunchSignatureResponse::getId).collect(Collectors.toList());
        responsePageInfo.setCurrent(page);
        responsePageInfo.setSize(size);
        responsePageInfo.setTotal(pageInfo.getTotal());
        if (giftIds.isEmpty()) {
            //?????????????????????
            responsePageInfo.setRecords(signatureResponses);
        }
        List<GiftVoucher> giftVouchers = giftVoucherMapper.selectList(new QueryWrapper<GiftVoucher>().lambda().in(GiftVoucher::getGiftId, giftIds));
        //2 ?????????????????????????????????
        Map<Long, List<GiftVoucher>> collect = giftVouchers.stream().collect(Collectors.groupingBy(GiftVoucher::getGiftId));
        List<Map.Entry<Long, List<GiftVoucher>>> entries = new ArrayList<>(collect.entrySet());
        //3.?????????????????????????????????
        //4 ????????????????????????
        Long key = 0L;
        for (Map.Entry<Long, List<GiftVoucher>> listEntry1 : entries) {
            for (Map.Entry<Long, List<GiftVoucher>> listEntry2 : entries) {
                if (listEntry1.getValue().size() > 2 * listEntry2.getValue().size()) {
                    key = listEntry1.getKey();
                    break;
                }
            }
        }

        //0 hot?????? 1??????hot??????
        for (GiftPunchSignatureResponse item : signatureResponses) {
            if (item.getId().equals(key)) {
                item.setHot(0);
            } else {
                item.setHot(1);
            }
        }

        //?????? hot??????????????????
        List<GiftPunchSignatureResponse> responses = signatureResponses.stream().sorted(Comparator.comparing(GiftPunchSignatureResponse::getHot)).collect(Collectors.toList());
        responsePageInfo.setRecords(responses);
        return responsePageInfo;

    }

    @Override
    public Boolean ordinaryExchange(String giftBagId) {
        return exchange(giftBagId, String.valueOf(GiftTypeEnum.NORMAL.getCode()), 1);
    }

    /**
     * ????????????
     *
     * @param giftBagId
     */
    @Override
    public Boolean luxuryExchange(String giftBagId) {
        return exchange(giftBagId, String.valueOf(GiftTypeEnum.LUXURY.getCode()), 1);
    }

    /**
     * @param giftBagId   ?????????id
     * @param type        ???????????? 1?????? 2??????
     * @param voucherType ????????? 1: ??????????????? 2: ???????????????
     * @return
     */
    private Boolean exchange(String giftBagId, String type, Integer voucherType) {
        synchronized (PunchSignatureServiceImpl.class) {
            UserInfo userInfo = JwtUtil.getInfo(UserInfo.class);
            //??????????????????
            ClockInUser byId = clockInUserService.getById(Long.parseLong(userInfo.getId()));
            if (byId == null || byId.getAuthId() == null) {
                log.info("?????????????????????????????????");
                throw new BasicException("?????????????????????????????????");
            }
            //1????????????????????????
            GiftBag giftBag = mongoTemplate.findOne(Query.query(Criteria.where("id").is(Long.parseLong(giftBagId))), GiftBag.class);
            if (giftBag == null) {
                log.info("??????????????? {}", giftBagId);
                throw new BasicException("???????????????");
            }
            //??????????????????????????????????????????
            if (giftBag.getMaxExNum() <= giftBag.getExchangedNum()) {
                log.info("??????{}????????????????????????", giftBagId);
                throw new BasicException("?????????????????????????????????????????????????????????????????????");
            }
            //????????????
            if (type.equals(String.valueOf(GiftTypeEnum.LUXURY.getCode()))) {
                //????????????????????????????????????
                List<Long> placeElement = giftBag.getPlaceElement();
                Query queryDestination = new Query();
                Criteria criteriaDestination = Criteria.where("id").in(placeElement);
                queryDestination.addCriteria(criteriaDestination);
                List<Destination> destinations = mongoTemplate.find(queryDestination, Destination.class);
                List<Long> destinationIds = destinations.stream().map(Destination::getId).collect(Collectors.toList());
                List<PunchLog> punchLogs = punchLogMapper.selectList(new QueryWrapper<PunchLog>().lambda().eq(PunchLog::getUserId, Long.parseLong(userInfo.getId())).in(PunchLog::getDestinationId, destinationIds));
                List<Long> destinationIdPunchLogs = punchLogs.stream().map(PunchLog::getDestinationId).collect(Collectors.toList());
                List<Long> tempDestinationIdPunchLogs = new ArrayList<>();
                // ???????????????????????????????????????????????????????????????
                for (Long destinationIdPunchLog : destinationIdPunchLogs) {
                    if (destinationIds.contains(destinationIdPunchLog)) {
                        tempDestinationIdPunchLogs.add(destinationIdPunchLog);
                    }
                }
                if (destinationIds.size() != tempDestinationIdPunchLogs.size()) {

                    throw new BasicException("????????????????????????");
                }
            }

            // TODO ??????????????????
            int scoreSum = 0;
            if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
                List<Long> giftBagIds = mongoTemplate.find(Query.query(Criteria.where("giftBagId").is(giftBag.getId())), GiftBagRelation.class).stream().map(GiftBagRelation::getGiftId).collect(Collectors.toList());
                List<Gift> gifts = mongoTemplate.find(Query.query(Criteria.where("id").in(giftBagIds)), Gift.class);
                scoreSum = gifts.stream().mapToInt(Gift::getGiftScore).sum();
                Long myScore = punchLogService.myScore();
                if (myScore.intValue() < scoreSum) {
                    throw new BasicException("????????????");
                }
            }

            //2??????????????????????????????
            LocalDateTime validDate = LocalDateTime.ofEpochSecond(giftBag.getValidDate(), 0, ZoneOffset.of("+8"));
            if (validDate.isBefore(LocalDateTime.now())) {
                log.info("??????????????? {}", giftBagId);
                throw new BasicException("???????????????");
            }


            LambdaQueryWrapper<GiftVoucher> queryWrapper = new QueryWrapper<GiftVoucher>().lambda().eq(GiftVoucher::getGiftId, Long.parseLong(giftBagId)).eq(GiftVoucher::getUserId, Long.parseLong(userInfo.getId()));
            List<GiftVoucher> voucher = giftVoucherMapper.selectList(queryWrapper);
            //3??????????????????????????????
            Integer exchangeCount = giftBag.getExchangeCount();

            if (type.equals(String.valueOf(GiftTypeEnum.LUXURY.getCode()))) {
                if (voucher != null) {
                    if (voucher.size() >= exchangeCount) {
                        log.info("????????????????????? {},?????????????????? {} ", voucher.size(), exchangeCount);
                        throw new BasicException("?????????????????????");
                    }
                }
            }

            if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
                //??????????????????????????????
                if (voucher != null) {
                    if (voucher.size() >= exchangeCount) {
                        log.info("????????????????????? {},?????????????????? {} ", voucher.size(), exchangeCount);
                        throw new BasicException("??????????????????????????????");
                    }
                }
            }


            long epochSecond = Instant.now().getEpochSecond();
            List<GiftVoucher> exchangeLimitVoucher = giftVoucherMapper.findAllByCreateTimeAndUserId(Long.parseLong(giftBagId), Long.parseLong(userInfo.getId()), epochSecond);
            //4??????????????????????????????????????????
            Integer exchangeLimit = giftBag.getExchangeLimit();

            if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
                if (exchangeLimitVoucher != null) {
                    log.info("???????????????????????????????????? {},?????????????????? {} ", exchangeLimitVoucher.size(), exchangeLimit);
                    if (exchangeLimitVoucher.size() >= exchangeLimit) {
                        throw new BasicException("????????????????????????????????????");
                    }
                }
            }

            long id = sequence.nextId();
            //???????????????????????????????????????
            String content = QRCodeContent.builder().id(String.valueOf(id)).app(AppEnum.outlets.name()).type(QRCodeSceneEnum.WRITE_OFF.name()).source(UserTypeEnum.CLOCK_IN.name()).build().toString();

            try {
                String qrcodeBase64 = QrcodeUtil.getQrcodeBase64(content);
                GiftVoucher giftVoucher = new GiftVoucher();
                giftVoucher.setId(id);
                giftVoucher.setGiftId(Long.parseLong(giftBagId));
                giftVoucher.setGiftName(giftBag.getName());
                giftVoucher.setGiftVoucherId(UUID.randomUUID().toString());
                //???????????????
                giftVoucher.setGiftVoucherType(voucherType);
                giftVoucher.setGiftVoucherQrcode(qrcodeBase64);
                giftVoucher.setUserId(Long.parseLong(userInfo.getId()));
                //???????????????
                giftVoucher.setGiftVoucherName(giftBag.getName());
                giftVoucher.setExchangeDeadline(giftBag.getValidDate());
                giftVoucher.setGiftName(giftBag.getName());
                giftVoucher.setState(0);

                //???????????????????????????????????????????????????????????????????????????????????????????????????
                if (type.equals(String.valueOf(GiftTypeEnum.LUXURY.getCode()))) {
                    //????????????
                    giftVoucher.setExchangeInstructions(String.format("????????????" + "(%s)", giftBag.getPlaceElement().size()));
                }
                if (type.equals(String.valueOf(GiftTypeEnum.NORMAL.getCode()))) {
                    //????????????
                    giftVoucher.setExchangeInstructions(String.format("????????????" + "(%s)", scoreSum));
                }

                long second = Instant.now().getEpochSecond();
                giftVoucher.setCreateTime(second);
                giftVoucher.setUpdateTime(second);
                int i = giftVoucherMapper.insert(giftVoucher);
                if (i == 0) {
                    log.info("?????????????????? {}", giftBagId);
                    return Boolean.FALSE;
                }
                //?????????????????????
                giftBag.setExchangedNum(giftBag.getExchangedNum() + 1);
                giftBag.setSub(giftBag.getMaxExNum() - giftBag.getExchangedNum());
                mongoTemplate.save(giftBag);
            } catch (Exception e) {
                log.info("????????????????????? {}", e.getMessage(), e);
                throw new BasicException("????????????");
            }
            return Boolean.TRUE;
        }
    }
}

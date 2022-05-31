package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.exception.BasicException;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.ActivityRule;
import com.outletcn.app.model.dto.applet.ActivityRuleResponse;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.mapper.ClockInUserMapper;
import com.outletcn.app.service.ClockInUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 打卡用户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2022-05-16
 */
@Service
public class ClockInUserServiceImpl extends ServiceImpl<ClockInUserMapper, ClockInUser> implements ClockInUserService {
    private final UserConverter userConverter;
    private final MongoTemplate mongoTemplate;

    public ClockInUserServiceImpl(UserConverter userConverter, MongoTemplate mongoTemplate) {
        this.userConverter = userConverter;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 客服电话
     */
    @Value("${system.phone}")
    private String phone;

    @Override
    public ApiResult<Boolean> updateUser(UpdateUserRequest updateUserRequest) {
        ClockInUser clockInUser = userConverter.toClockInUser(updateUserRequest);
        UserInfo info = JwtUtil.getInfo(UserInfo.class);
        Assert.notNull(info, "通过token获取用户信息失败");
        boolean update = update(clockInUser, new UpdateWrapper<ClockInUser>().lambda().eq(ClockInUser::getOpenId, info.getOpenId()));
        return ApiResult.ok(update);
    }

    @Override
    public ActivityRuleResponse activity() {
        List<GiftBag> giftBags = buildGiftBag();
        return ActivityRuleResponse.builder()
                .existLuxuryGiftBag(!giftBags.isEmpty())
                .activityRule(buildActivityRule())
                .images(giftBags.stream().map(GiftBag::getRecommendImage).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<GiftBag> buildGiftBag() {
        Query type = Query.query(Criteria.where("type").is(2).and("putOn").is(0));
        return mongoTemplate.find(type, GiftBag.class);
    }

    @Override
    public String contactCustomerService() {
        if (phone == null) {
            throw new BasicException("联系客户电话未配置");
        }
        return phone;
    }

    private ActivityRule buildActivityRule() {
        //todo 构建活动规则
        List<Map<String, Object>> descriptions = new ArrayList<>(8);
       /* Map<String, Object> descriptionC = new LinkedHashMap<>();
        descriptionC.put("type", "video");
        descriptionC.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/510792601-1-208.mp4");
        descriptions.add(descriptionC);*/

        /*Map<String, Object> descriptionD = new LinkedHashMap<>();
        descriptionD.put("type", "audio");
        descriptionD.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/25d9ca5fc88747d89dcef66b04670482.mp3");
        descriptions.add(descriptionD);*/


       /* Map<String, Object> descriptionE = new LinkedHashMap<>();
        descriptionE.put("type", "text");
        descriptionE.put("content", "1990年海湾战争后，世界进入了一个短暂的相对和平时期。家驹便写了《AMANI》这首歌，抒发了对战后和平长久的渴望,也警示了人们必须要以自己的努力斗争来争取和平，一味地求助于神灵是不行的。这首歌堪称是颂和平歌之最。另外，有些网友可能查不出歌词中的“英文”是什么意思，因为这是非洲国家肯尼亚(Kenya)的语言，“AMANI”是和平的意思；“ NAKUPENDA NAKUPENDA WEWE”就是“我们爱你”“TUNE TAKE WE WE”是“我们需要你”的意思。");
        descriptions.add(descriptionE);*/
        
       /* Map<String, Object> descriptionB = new LinkedHashMap<>();
        descriptionB.put("type", "image");
        descriptionB.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/c57c48e5829942ee80df36d59eb9f566.png");
        descriptions.add(descriptionB);*/

        //1. 构建活动1
        Map<String, Object> description1 = new LinkedHashMap<>();
        description1.put("type", "title");
        description1.put("content", "活动一：点亮新奥莱  「超级大礼」");
        descriptions.add(description1);

        Map<String, Object> description2 = new LinkedHashMap<>();
        description2.put("type", "text");
        description2.put("content", "活动时间：2022年7月1日-12月31日");
        descriptions.add(description2);

        Map<String, Object> description3 = new LinkedHashMap<>();
        description3.put("type", "text");
        description3.put("content", "参与方式：\n" +
                "1、所有用户均可参与，每个用户限参与一次；\n" +
                "2、在指定地点扫码打卡，可点亮卡牌。点亮所有卡牌，可领取礼品兑换券，凭券兑换礼品；\n" +
                "3、本活动需在指定礼品兑换点进行兑换，详见礼品活动详情页；");
        descriptions.add(description3);

        Map<String, Object> description4 = new LinkedHashMap<>();
        description4.put("type", "text");
        description4.put("content", "其他：\n" +
                "1、如发现作弊行为，新奥莱有权采取相应措施；\n" +
                "2、本活动不和其他活动叠加；\n" +
                "3、其他未尽事宜，请详细咨询客服。新奥莱对此拥有最终解释权。");
        descriptions.add(description4);


        Map<String, Object> description5 = new LinkedHashMap<>();
        description5.put("type", "title");
        description5.put("content", "活动二：打卡游关岭");
        descriptions.add(description5);

        Map<String, Object> description6 = new LinkedHashMap<>();
        description6.put("type", "text");
        description6.put("content", "活动时间：2022年7月1日-12月31日");
        descriptions.add(description6);

        Map<String, Object> description7 = new LinkedHashMap<>();
        description7.put("type", "text");
        description7.put("content", "参与方式：\n" +
                "1、所有用户均可参与，每个用户限参与一次；\n" +
                "2、在指定地点扫码打卡，可得指定数量签章。签章可领取礼品兑换券，凭券兑换礼品；");
        descriptions.add(description7);

        Map<String, Object> description8 = new LinkedHashMap<>();
        description8.put("type", "text");
        description8.put("content", "其他：\n" +
                "1、如发现作弊行为，新奥莱有权采取相应措施；\n" +
                "2、本活动不和其他活动叠加；\n" +
                "3、其他未尽事宜，请详细咨询客服。新奥莱对此拥有最终解释权。");
        descriptions.add(description8);

        ActivityRule activityRule = new ActivityRule();
        //activityRule.setRecommendAudio("https://test-1311883259.cos.ap-chongqing.myqcloud.com/25d9ca5fc88747d89dcef66b04670482.mp3");
        //activityRule.setRecommendVideo("https://test-1311883259.cos.ap-chongqing.myqcloud.com/510792601-1-208.mp4");
        activityRule.setRecommendAudio("");
        activityRule.setRecommendVideo("");
        activityRule.setDescriptions(descriptions);
        return activityRule;
    }
}

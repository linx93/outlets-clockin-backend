package com.outletcn.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.outletcn.app.common.ApiResult;
import com.outletcn.app.common.PageInfo;
import com.outletcn.app.converter.UserConverter;
import com.outletcn.app.model.dto.UserInfo;
import com.outletcn.app.model.dto.applet.ActivityRule;
import com.outletcn.app.model.dto.applet.ActivityRuleResponse;
import com.outletcn.app.model.dto.applet.DetailsVO;
import com.outletcn.app.model.dto.applet.UpdateUserRequest;
import com.outletcn.app.model.dto.gift.LuxuryGiftBagResponse;
import com.outletcn.app.model.mongo.Gift;
import com.outletcn.app.model.mongo.GiftBag;
import com.outletcn.app.model.mysql.ClockInUser;
import com.outletcn.app.mapper.ClockInUserMapper;
import com.outletcn.app.service.ClockInUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.outletcn.app.service.gift.GiftService;
import com.outletcn.app.utils.JwtUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class ClockInUserServiceImpl extends ServiceImpl<ClockInUserMapper, ClockInUser> implements ClockInUserService {
    private final UserConverter userConverter;
    private final MongoTemplate mongoTemplate;

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
        Query type = Query.query(Criteria.where("type").is(2));
        return mongoTemplate.find(type, GiftBag.class);
    }

    private ActivityRule buildActivityRule() {
        //todo 构建活动规则
        List<Map<String, Object>> descriptions = new ArrayList<>(8);
        Map<String, Object> descriptionC = new LinkedHashMap<>();
        descriptionC.put("type", "video");
        descriptionC.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/510792601-1-208.mp4");
        descriptions.add(descriptionC);

        Map<String, Object> descriptionD = new LinkedHashMap<>();
        descriptionD.put("type", "audio");
        descriptionD.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/25d9ca5fc88747d89dcef66b04670482.mp3");
        descriptions.add(descriptionD);
        
        Map<String, Object> descriptionB = new LinkedHashMap<>();
        descriptionB.put("type", "image");
        descriptionB.put("content", "https://test-1311883259.cos.ap-chongqing.myqcloud.com/c57c48e5829942ee80df36d59eb9f566.png");
        descriptions.add(descriptionB);

        Map<String, Object> descriptionA = new LinkedHashMap<>();
        descriptionA.put("type", "text");
        descriptionA.put("content", "1990年海湾战争后，世界进入了一个短暂的相对和平时期。家驹便写了《AMANI》这首歌，抒发了对战后和平长久的渴望,也警示了人们必须要以自己的努力斗争来争取和平，一味地求助于神灵是不行的。这首歌堪称是颂和平歌之最。另外，有些网友可能查不出歌词中的“英文”是什么意思，因为这是非洲国家肯尼亚(Kenya)的语言，“AMANI”是和平的意思；“ NAKUPENDA NAKUPENDA WEWE”就是“我们爱你”“TUNE TAKE WE WE”是“我们需要你”的意思。　1991年2月1日到8月，Beyond应世界宣明会之邀远赴战火中的非洲，探访第三世界的穷困人民，开始了一段非洲之旅，目的地是非洲的新几内亚和肯尼亚，在卢旺达晒成一只非洲鸡。Beyond第一次从事慈善的探访时间，并成立了一个第三世界基金，并且在归来后，把回忆变成一曲真诚彻骨、蕴含着世界大同思想的新曲“AMANI”。还写了一首《光辉岁月》送给为自由拼搏终身的非洲总统曼德拉，反对种族歧视，希望世界和平，这一直都是家驹的心愿，家驹也因《光辉岁月》成了年度最佳填词人，Beyond也成为香港世界宣明会的代言人。在一次接受采访时，家驹说：“我去看第三世界不单只看贫穷，而是看第三世界的改变，看未来的第叁世界，以人类的良知为出发点，用感性反思人的所作所为。非洲向来予人穷困和落后的感觉，我们觉得微不足道的物质，在那里也许会成为很有意义的物质。世界不断前进，眼看他们的生命停滞下来，不禁觉得可惜。只要我们肯付出一点关注，他们也可以跟我们迈进明天");
        descriptions.add(descriptionA);


        ActivityRule activityRule = new ActivityRule();
        activityRule.setRecommendAudio("https://test-1311883259.cos.ap-chongqing.myqcloud.com/25d9ca5fc88747d89dcef66b04670482.mp3");
        activityRule.setRecommendVideo("https://test-1311883259.cos.ap-chongqing.myqcloud.com/510792601-1-208.mp4");
        activityRule.setDescriptions(descriptions);
        return activityRule;
    }
}

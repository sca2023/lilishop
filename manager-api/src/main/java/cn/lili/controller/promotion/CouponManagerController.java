package cn.lili.controller.promotion;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.PageUtil;
import cn.lili.common.utils.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.promotion.entity.dos.Coupon;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.entity.enums.PromotionStatusEnum;
import cn.lili.modules.promotion.entity.vos.CouponSearchParams;
import cn.lili.modules.promotion.entity.vos.CouponVO;
import cn.lili.modules.promotion.service.CouponService;
import cn.lili.modules.promotion.service.MemberCouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理端,优惠券接口
 *
 * @author paulG
 * @date 2020/10/9
 **/
@RestController
@Api(tags = "管理端,优惠券接口")
@RequestMapping("/manager/promotion/coupon")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CouponManagerController {

    private final CouponService couponService;
    private final MemberCouponService memberCouponService;

    @ApiOperation(value = "获取优惠券列表")
    @GetMapping
    public ResultMessage<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
        page.setNotConvert(true);
        queryParam.setStoreId("platform");
        IPage<CouponVO> coupons = couponService.getCouponsByPageFromMongo(queryParam, page);
        return ResultUtil.data(coupons);
    }

    @ApiOperation(value = "获取优惠券详情")
    @GetMapping("/{couponId}")
    public ResultMessage<CouponVO> getCoupon(@PathVariable String couponId) {
        CouponVO coupon = couponService.getCouponDetailFromMongo(couponId);
        return ResultUtil.data(coupon);
    }

    @ApiOperation(value = "添加优惠券")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<CouponVO> addCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        if (couponService.add(couponVO) != null) {
            return ResultUtil.data(couponVO);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }

    @ApiOperation(value = "修改优惠券")
    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<Coupon> updateCoupon(@RequestBody CouponVO couponVO) {
        Coupon coupon = couponService.getById(couponVO.getId());
        couponVO.setPromotionStatus(PromotionStatusEnum.NEW.name());
        if (couponService.updateCoupon(couponVO) != null) {
            return ResultUtil.data(coupon);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }

    @ApiOperation(value = "修改优惠券状态")
    @PutMapping("/status")
    public ResultMessage<Object> updateCouponStatus(String couponIds, String promotionStatus) {
        String[] split = couponIds.split(",");
        if (couponService.updateCouponStatus(Arrays.asList(split), PromotionStatusEnum.valueOf(promotionStatus))) {
            return ResultUtil.success(ResultCode.COUPON_EDIT_STATUS_SUCCESS);
        }
        return ResultUtil.error(ResultCode.COUPON_EDIT_STATUS_ERROR);
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        for (String id : ids) {
            couponService.deleteCoupon(id);
        }
        return ResultUtil.success(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "会员优惠券作废")
    @PutMapping(value = "/member/cancellation/{id}")
    public ResultMessage<Object> cancellation(@PathVariable String id) {
        memberCouponService.cancellation(id);
        return ResultUtil.success(ResultCode.COUPON_CANCELLATION_SUCCESS);
    }

    @ApiOperation(value = "根据优惠券id券分页获取会员领详情")
    @GetMapping(value = "/member/{id}")
    public ResultMessage<IPage<MemberCoupon>> getByPage(@PathVariable String id,
                                                        PageVO page) {
        QueryWrapper<MemberCoupon> queryWrapper = new QueryWrapper<>();
        IPage<MemberCoupon> data = memberCouponService.page(PageUtil.initPage(page),
                queryWrapper.eq("coupon_id", id)
        );
        return ResultUtil.data(data);

    }

    private void setStoreInfo(CouponVO couponVO) {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException("获取当前用户信息不存在");
        }
        couponVO.setStoreId("platform");
        couponVO.setStoreName("platform");
    }

}
package cn.lili.controller.statistics;

import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.statistics.model.dto.GoodsStatisticsQueryParam;
import cn.lili.modules.statistics.model.vo.GoodsStatisticsDataVO;
import cn.lili.modules.statistics.service.GoodsStatisticsDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 店铺端,商品统计接口
 *
 * @author Bulbasaur
 * @date: 2020/11/22 14:23
 */
@Api(tags = "店铺端,商品统计接口")
@RestController
@RequestMapping("/store/statistics/goods")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsStatisticsStoreController {

    /**
     * 商品统计
     */
    private final GoodsStatisticsDataService goodsStatisticsDataService;

    @ApiOperation(value = "获取统计列表,排行前一百的数据")
    @GetMapping
    public ResultMessage<List<GoodsStatisticsDataVO>> getByPage(GoodsStatisticsQueryParam statisticsQueryParam) {
        statisticsQueryParam.setStoreId(UserContext.getCurrentUser().getStoreId());
        return ResultUtil.data(goodsStatisticsDataService.getGoodsStatisticsData(statisticsQueryParam, 100));
    }
}
package cn.lili.controller.goods;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.utils.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.CategoryParameterGroup;
import cn.lili.modules.goods.entity.dos.Parameters;
import cn.lili.modules.goods.entity.vos.ParameterGroupVO;
import cn.lili.modules.goods.service.CategoryParameterGroupService;
import cn.lili.modules.goods.service.ParametersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,分类绑定参数组接口
 *
 * @author pikachu
 * @date 2020-02-18 15:18:56
 */
@RestController
@Api(tags = "管理端,分类绑定参数组接口")
@RequestMapping("/manager/goods/category/parameters")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryParameterGroupManagerController {

    /**
     * 参数组
     */
    private final ParametersService parametersService;

    /**
     * 分类参数
     */
    private final CategoryParameterGroupService categoryParameterGroupService;

    @ApiOperation(value = "查询某分类下绑定的参数信息")
    @GetMapping(value = "/{categoryId}")
    @ApiImplicitParam(value = "分类id", required = true, dataType = "String", paramType = "path")
    public ResultMessage<List<ParameterGroupVO>> getCategoryParam(@PathVariable String categoryId) {
        return ResultUtil.data(categoryParameterGroupService.getCategoryParams(categoryId));
    }

    @ApiOperation(value = "保存数据")
    @PostMapping
    public ResultMessage<CategoryParameterGroup> saveOrUpdate(CategoryParameterGroup categoryParameterGroup) {

        if (categoryParameterGroupService.save(categoryParameterGroup)) {
            return ResultUtil.data(categoryParameterGroup);
        }
        return ResultUtil.error(ResultCode.CATEGORY_PARAMETER_SAVE_ERROR);
    }

    @ApiOperation(value = "更新数据")
    @PutMapping
    public ResultMessage<CategoryParameterGroup> update(CategoryParameterGroup categoryParameterGroup) {

        if (categoryParameterGroupService.updateById(categoryParameterGroup)) {
            return ResultUtil.data(categoryParameterGroup);
        }
        return ResultUtil.error(ResultCode.CATEGORY_PARAMETER_UPDATE_ERROR);
    }

    @ApiOperation(value = "通过id删除参数组")
    @ApiImplicitParam(name = "id", value = "参数组ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public ResultMessage<Object> delAllByIds(@PathVariable String id) {
        //删除参数
        parametersService.remove(new QueryWrapper<Parameters>().eq("group_id", id));
        //删除参数组
        categoryParameterGroupService.removeById(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
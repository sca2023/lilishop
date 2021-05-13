package cn.lili.controller.other;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.utils.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.page.entity.dos.Article;
import cn.lili.modules.page.entity.dto.ArticleSearchParams;
import cn.lili.modules.page.entity.enums.ArticleEnum;
import cn.lili.modules.page.entity.vos.ArticleVO;
import cn.lili.modules.page.service.ArticleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端,文章接口
 *
 * @author pikachu
 * @date 2020-05-06 15:18:56
 */
@RestController
@Api(tags = "管理端,文章接口")
@RequestMapping("/manager/article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleManagerController {

    /**
     * 文章
     */
    private final ArticleService articleService;

    @ApiOperation(value = "查看文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public ResultMessage<Article> get(@PathVariable String id) {

        return ResultUtil.data(articleService.getById(id));
    }

    @ApiOperation(value = "分页获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "文章分类ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<ArticleVO>> getByPage(ArticleSearchParams articleSearchParams) {
        return ResultUtil.data(articleService.articlePage(articleSearchParams));
    }

    @ApiOperation(value = "添加文章")
    @PostMapping
    public ResultMessage<Article> save(@Valid Article article) {
        article.setType(ArticleEnum.OTHER.name());
        articleService.save(article);
        return ResultUtil.data(article);
    }

    @ApiOperation(value = "修改文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("update/{id}")
    public ResultMessage<Article> update(@Valid Article article, @PathVariable("id") String id) {
        article.setId(id);
        return ResultUtil.data(articleService.updateArticle(article));
    }

    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delByIds/{id}")
    public ResultMessage<Object> delAllByIds(@PathVariable String id) {
        articleService.customRemove(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


}
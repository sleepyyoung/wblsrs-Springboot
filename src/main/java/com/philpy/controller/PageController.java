package com.philpy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "页面控制器")
@RequestMapping("/")
@Controller
public class PageController {
    @GetMapping("/")
    @ApiOperation("首页")
    public String getWblsrs() {
        return "wblsrs";
    }

    @ApiOperation("按照时间查询页面")
    @GetMapping("/query-by-time")
    public String queryByTime() {
        return "queryByTime";
    }

    @ApiOperation("按照内容模糊查询页面")
    @GetMapping("/fuzzy-query-by-content")
    public String fuzzyQueryByTime() {
        return "fuzzyQueryByContent";
    }

    @ApiOperation("按照内容精确查询页面")
    @GetMapping("/precise-query-by-content")
    public String preciseQueryByTime() {
        return "preciseQueryByContent";
    }

}

/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.dinky.controller;

import org.dinky.data.model.Jar;
import org.dinky.data.model.Task;
import org.dinky.data.result.ProTableResult;
import org.dinky.data.result.Result;
import org.dinky.function.constant.PathConstant;
import org.dinky.function.data.model.UDF;
import org.dinky.function.util.UDFUtil;
import org.dinky.service.JarService;
import org.dinky.service.TaskService;

import org.apache.flink.table.catalog.FunctionLanguage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JarController
 *
 * @since 2021/11/13
 */
@Slf4j
@RestController
@RequestMapping("/api/jar")
@RequiredArgsConstructor
public class JarController {

    private final JarService jarService;

    private final TaskService taskService;

    /** 新增或者更新 */
    @PutMapping
    public Result<Void> saveOrUpdate(@RequestBody Jar jar) throws Exception {
        if (jarService.saveOrUpdate(jar)) {
            return Result.succeed("新增成功");
        } else {
            return Result.failed("新增失败");
        }
    }

    /** 动态查询列表 */
    @PostMapping
    public ProTableResult<Jar> listJars(@RequestBody JsonNode para) {
        return jarService.selectForProTable(para);
    }

    /** 获取可用的jar列表 */
    @GetMapping("/listEnabledAll")
    @ApiOperation("Query jar list enabled all")
    public Result<List<Jar>> listEnabledAll() {
        List<Jar> jars = jarService.listEnabledAll();
        return Result.succeed(jars, "获取成功");
    }

    @PostMapping("/udf/generateJar")
    @ApiOperation("Generate jar")
    public Result<Map<String, List<String>>> generateJar() {
        List<Task> allUDF = taskService.getAllUDF();
        List<UDF> udfCodes = allUDF.stream()
                .map(task -> UDF.builder()
                        .code(task.getStatement())
                        .className(task.getSavePointPath())
                        .functionLanguage(
                                FunctionLanguage.valueOf(task.getDialect().toUpperCase()))
                        .build())
                .collect(Collectors.toList());
        Map<String, List<String>> resultMap = UDFUtil.buildJar(udfCodes);
        String msg = StrUtil.format(
                "udf jar生成成功，jar文件在{}；\n本次成功 class:{}。\n失败 class:{}",
                PathConstant.UDF_JAR_TMP_PATH,
                resultMap.get("success"),
                resultMap.get("failed"));
        return Result.succeed(resultMap, msg);
    }
}

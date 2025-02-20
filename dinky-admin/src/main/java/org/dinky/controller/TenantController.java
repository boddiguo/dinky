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

import org.dinky.data.annotation.Log;
import org.dinky.data.enums.BusinessType;
import org.dinky.data.model.Tenant;
import org.dinky.data.model.User;
import org.dinky.data.params.AssignUserToTenantParams;
import org.dinky.data.result.ProTableResult;
import org.dinky.data.result.Result;
import org.dinky.service.TenantService;
import org.dinky.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import cn.hutool.core.lang.Dict;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** tenant controller */
@Slf4j
@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final UserService userService;

    /**
     * save or update tenant
     *
     * @param tenant {@link Tenant}
     * @return {@link Result} of {@link Void}
     */
    @PutMapping
    @ApiOperation("Insert Or Update Tenant")
    @Log(title = "Insert Or Update Tenant", businessType = BusinessType.INSERT_OR_UPDATE)
    public Result<Void> saveOrUpdateTenant(@RequestBody Tenant tenant) {
        return tenantService.saveOrUpdateTenant(tenant);
    }

    /**
     * delete tenant by id
     *
     * @param tenantId tenant id
     * @return {@link Result} of {@link Void}
     */
    @DeleteMapping("/delete")
    @ApiOperation("Delete Tenant By Id")
    @Log(title = "Delete Tenant By Id", businessType = BusinessType.DELETE)
    public Result<Void> removeTenantById(@RequestParam("id") Integer tenantId) {
        return tenantService.removeTenantById(tenantId);
    }

    /**
     * list tenants
     *
     * @param para {@link JsonNode}
     * @return {@link ProTableResult} of {@link Tenant}
     */
    @PostMapping
    @ApiOperation("List Tenants")
    public ProTableResult<Tenant> listTenants(@RequestBody JsonNode para) {
        return tenantService.selectForProTable(para, true);
    }

    /**
     * assign user to tenant
     *
     * @param assignUserToTenantParams {@link AssignUserToTenantParams}
     * @return {@link Result} of {@link Void}
     */
    @PutMapping(value = "/assignUserToTenant")
    @ApiOperation("Assign User To Tenant")
    @Log(title = "Assign User To Tenant", businessType = BusinessType.INSERT)
    public Result<Void> assignUserToTenant(@RequestBody AssignUserToTenantParams assignUserToTenantParams) {
        return tenantService.assignUserToTenant(assignUserToTenantParams);
    }

    /**
     * get user list by tenant id
     *
     * @param id
     * @return {@link Result} with {@link Dict}
     */
    @GetMapping("/getUsersByTenantId")
    @ApiOperation("Get User List By Tenant Id")
    public Result<List<User>> getUserListByTenantId(@RequestParam("id") Integer id) {
        return Result.succeed(userService.getUserListByTenantId(id));
    }
}

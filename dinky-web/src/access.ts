/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { API } from './services/data';

/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser.user.superAdminFlag,
    canAuth({ path, ...route }) {
      if (currentUser && currentUser.user.superAdminFlag) {
        return true;
      }

      //TODO根据path判断应用、目录、菜单、按钮，判断返回true,false
      //TODOpath可以是window.location.href、key

      return currentUser?.menuList?.some?.(
        (item) => item?.path?.startsWith(path) || item?.path?.endsWith(path),
      );
    },
  };
}

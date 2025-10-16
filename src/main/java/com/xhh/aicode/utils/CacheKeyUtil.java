package com.xhh.aicode.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

/**
 * 缓存 key 生成工具类
 */
public class CacheKeyUtil {

    /**
     * 根据对象生成缓存 key（JSON + MD5）
     *
     * @param obj       要生成 key 的对象
     * @return          MD5 哈希后的缓存 key
     */
    public static String generateKey(Object obj) {
        if (obj == null) {
            return DigestUtil.md5Hex("null");
        }
        // 先转JSON，再进行 md5 编码
        String jsonStr = JSONUtil.toJsonStr(obj);
        return DigestUtil.md5Hex(jsonStr);
    }

}

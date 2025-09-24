package com.xhh.aicode.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class CodeFileSaverTemplate<T> {

    // 文件保存根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    public final File saveCode(T result, Long appId){
        // 1. 验证输入
        validateInput(result);
        // 2. 构建唯一目录
        String baseDirPath = buildUniqueDir(appId);
        // 3. 保存文件（由具体的子类实现）
        saveFiles(result, baseDirPath);
        // 4. 返回目录文件对象
        return new File(baseDirPath);
    }

    /**
     * 验证输入参数（可由子类覆盖）
     *
     * @param result    代码结果对象
     */
    protected void validateInput(T result){
        if (result == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码结果对象不能为空");
    }

    /**
     * 构建唯一目录路径：temp/code_output/bizType_雪花Id
     *
     * @return          唯一目录路径
     */
    private String buildUniqueDir(Long appId) {
        ThrowUtils.throwIf(appId == null, ErrorCode.SYSTEM_ERROR, "应用 ID 不能为空");
        String bizType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", bizType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 写入单个文件
     *
     * @param dirPath       目录路径
     * @param fileName      文件名称
     * @param content       文件内容
     */
    protected final void writeToFile(String dirPath, String fileName, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + fileName;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 保存代码结果文件（由子类实现）
     * @param result        代码结果对象
     * @param baseDirPath   文件保存的路径
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * 获取代码类型（由子类实现）
     *
     * @return  CodeGenTypeEnum: 代码类型枚举
     */
    protected abstract CodeGenTypeEnum getCodeType();

}
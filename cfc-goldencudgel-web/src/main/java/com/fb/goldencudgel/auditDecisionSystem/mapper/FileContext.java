package com.fb.goldencudgel.auditDecisionSystem.mapper;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 附件内容表
 * @Auther hu
 */
@Document(collection ="FB_FILE_CONTEXT")
public class FileContext implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "FILE_ID")
    private String fileId;//附件id

    @Column(name = "FILE_CONTEXT")
    private String fileContext;//附件内容

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileContext() {
        return fileContext;
    }

    public void setFileContext(String fileContext) {
        this.fileContext = fileContext;
    }
}

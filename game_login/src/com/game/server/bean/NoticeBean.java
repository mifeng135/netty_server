package com.game.server.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class NoticeBean implements Serializable {
    private int noticeId;
    private String content;
}

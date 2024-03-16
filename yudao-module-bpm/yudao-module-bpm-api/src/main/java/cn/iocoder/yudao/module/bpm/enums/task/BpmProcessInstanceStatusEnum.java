package cn.iocoder.yudao.module.bpm.enums.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程实例 ProcessInstance 的状态
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmProcessInstanceStatusEnum {

    RUNNING(1, "进行中"),
    APPROVE(2, "通过"),
    REJECT(3, "不通过"),
    CANCEL(4, "已取消");

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String desc;

}

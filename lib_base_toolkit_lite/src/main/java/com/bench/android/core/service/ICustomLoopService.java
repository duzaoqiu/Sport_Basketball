package com.bench.android.core.service;

/**
 * 自定义服务，如果只是在App内调用Android的Service，用于做一些任务的调用，比如
 * 轮询服务，之前轮询服务单独放在一个Service里面，但是android8.0之后，默认服务为
 * 前台服务，导致app退到后台，就会有一个通知栏显示app正在运行，导致用户体验不好
 *
 * @author zhouyi
 */

public interface ICustomLoopService { }

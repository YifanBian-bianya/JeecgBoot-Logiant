package com.logiant.modules.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.logiant.modules.system.entity.SysAnnouncementSend;
import com.logiant.modules.system.mapper.SysAnnouncementSendMapper;
import com.logiant.modules.system.model.AnnouncementSendModel;
import com.logiant.modules.system.service.ISysAnnouncementSendService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
@Service
public class SysAnnouncementSendServiceImpl extends ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements ISysAnnouncementSendService {

	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;
	
	@Override
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
			AnnouncementSendModel announcementSendModel) {
		 return page.setRecords(sysAnnouncementSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
	}

	@Override
	public AnnouncementSendModel getOne(String sendId) {
		return sysAnnouncementSendMapper.getOne(sendId);
	}

}

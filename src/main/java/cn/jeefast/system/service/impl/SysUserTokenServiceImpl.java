package cn.jeefast.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.jeefast.common.utils.R;
import cn.jeefast.system.dao.SysUserTokenDao;
import cn.jeefast.system.entity.SysUserToken;
import cn.jeefast.system.oauth2.TokenGenerator;
import cn.jeefast.system.service.SysUserTokenService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户Token 服务实现类
 * </p>
 *
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserToken> implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;
	
	@Override
	public SysUserToken queryByToken(String token) {
		return sysUserTokenDao.queryByToken(token);
	}
	
	@Override
	public R createToken(long userId) {
		System.out.println("userIduserIduserId"+userId);
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		//如果已存在 (tokenEntity != null)，则更新现有的token和过期时间
		System.out.println("userIduserIduserId"+userId);
//		SysUserToken tokenEntity = sysUserTokenDao.selectById(userId);
		SysUserToken tokenEntityOld = new SysUserToken();
		tokenEntityOld.setUserId(userId);
		SysUserToken tokenEntity = sysUserTokenDao.selectOne(tokenEntityOld);
		System.out.println("tokenEntitytokenEntitytokenEntity"+tokenEntity);
		if(tokenEntity == null){
			tokenEntity = new SysUserToken();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			sysUserTokenDao.insert(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			sysUserTokenDao.updateById(tokenEntity);
		}

		//创建一个成功响应的 R 对象，将生成的token和过期时间放入响应中，并返回给调用者
		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}
	
}

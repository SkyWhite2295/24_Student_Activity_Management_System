package cn.jeefast.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import cn.jeefast.common.exception.RRException;
import cn.jeefast.system.entity.SysUser;

/**
 * Shiro工具类
 * 
 */
public class ShiroUtils {

	//返回当前用户的会话 Session 对象
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	//返回当前用户的 Subject 对象，即表示当前用户的安全操作
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	//返回当前登录用户的 SysUser 对象
	public static SysUser getUser() {
		return (SysUser) SecurityUtils.getSubject().getPrincipal();
	}

	public static Long getUserId() {
		return getUser().getUserId();
	}

	//向当前用户的会话中设置属性值
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	//获取当前用户会话中指定属性 key 的值
	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	//判断当前用户是否已登录
	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	//获取验证码。首先从会话中获取指定 key 的验证码对象，
	// 如果获取不到则抛出 RRException 异常表示验证码已失效。
	// 获取到验证码后，从会话中移除该验证码，并将其转换为字符串返回。
	public static String getKaptcha(String key) {
		Object kaptcha = getSessionAttribute(key);
		if(kaptcha == null){
			throw new RRException("验证码已失效，请单击图片刷新");
		}
		getSession().removeAttribute(key);
		return kaptcha.toString();
	}

}

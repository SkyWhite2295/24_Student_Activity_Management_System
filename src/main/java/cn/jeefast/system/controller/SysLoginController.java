package cn.jeefast.system.controller;

import cn.jeefast.system.entity.SysUserRole;
import cn.jeefast.system.entity.SysZcyxyzm;
import cn.jeefast.system.service.SysUserRoleService;
import cn.jeefast.system.service.SysZcyxyzmService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import cn.jeefast.common.utils.R;
import cn.jeefast.common.utils.ShiroUtils;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.service.SysUserService;
import cn.jeefast.system.service.SysUserTokenService;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 登录相关
 *
 */
@RestController
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysUserRoleService sysUserRoleService;


	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {//参数为HttpServletResponse对象，用于向客户端发送HTTP响应
		//设置响应头，指示客户端不要缓存此响应，确保每次请求获取的都是最新生成的验证码
		response.setHeader("Cache-Control", "no-store, no-cache");
		//设置响应内容的类型为image/jpeg，即告诉客户端返回的是JPEG格式的图片
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session(key,value)key:标识存储在会话中的验证码的键值
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		//获取响应的输出流，用于将生成的验证码图片写入到客户端
		ServletOutputStream out = response.getOutputStream();
		//使用Java标准库中的ImageIO工具将生成的验证码图片image以JPEG格式写入到输出流out中，从而返回给客户端
		ImageIO.write(image, "jpg", out);
		//关闭输出流，确保资源释放
		IOUtils.closeQuietly(out);
	}

	/**
	 * 密码找回
	 */
	@RequestMapping(value = "/sys/zhaohui", method = RequestMethod.POST)
//	public Map<String, Object> zhaohui(String sxh, String sname,
//									   String xzraol, String susername, String zcspassword
//			, String college, String major, String classinfo, String grade
//			,String email) throws Exception {
	public Map<String, Object> zhaohui(String susername,String email) throws Exception {

		if(email == null ||email.equals("")){
			return R.error().put("msg","请输入邮箱！");
			//return R.error().put("msg", "请联系管理员");
		}

		SysUser sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("username",susername));
		if(sysUser != null){
			if(email.equals(sysUser.getEmail())){
                Random random = new Random();
                int randomNumber = 100000 + random.nextInt(900000); // 生成介于100000到999999之间的随机数
				//int newNumber = 123456;
                System.out.println("随机6位数: " + randomNumber);

			    String spassword = randomNumber+"";
				//String spassword = "123456";
                System.out.println("spasswordspasswordspassword"+spassword);
			    sysUser.setPassword(spassword);
				System.out.println("aaaaaaaasysUsersysUsersysUser"+sysUser);
				List<Long> roleLongs = new ArrayList<>();
				List<SysUserRole> roleList = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",sysUser.getUserId()));
				if(roleList.size()>0){
					for(int i=0;i<roleList.size();i++){
						roleLongs.add(roleList.get(i).getRoleId());
					}
				}
				sysUser.setRoleIdList(roleLongs);
				sysUserService.update(sysUser);

                if(sysUser.getEmail() != null && !(sysUser.getEmail().trim()).equals("")){
                    String tegex = "[a-zA-Z0-9_]+@\\w+(\\.com|\\.cn){1}";
                    boolean flag=sysUser.getEmail().matches(tegex);
                    if(!flag){
                        return R.error().put("msg","邮箱格式不合法");
                    }

                }else{
                    return R.error().put("msg","请输入邮箱");
                }

                if(sysUser.getEmail() != null){
                    sendSimpleMail("您的密码已重置为："+spassword,sysUser.getEmail());
                    System.out.println("1111111111111111111");
                }
				return R.ok().put("msg","密码已初始并发至邮箱");
			}else{
				return R.error().put("msg","绑定的邮箱不正确");
			}
		}else{
			return R.error().put("msg","账号不存在无法找回。请正确输入账号");
		}
		//return R.error().put("msg", "请联系管理员");
	}

//	/**
//	 * 注册
//	 */
//	@RequestMapping(value = "/sys/regsave", method = RequestMethod.POST)
//	public Map<String, Object> regsave(String sxh, String sname,
//									   String xzraol, String susername, String spassword, String zcspassword
//			, String college, String major, String classinfo, String grade
//			, String yxyzm
//			, String email,String mibaowenti,String mibaowentidaan)throws IOException {
//		if(mibaowenti == null || mibaowentidaan == null || mibaowenti.equals("") || mibaowentidaan.equals("")){
//			return R.error().put("msg","请输入密保问题及答案！");
//		}
//		if(!spassword.equals(zcspassword)){
//			return R.error().put("msg","两次密码输入不一致");
//		}
//		if(yxyzm == null || yxyzm.trim().equals("")){
//			return R.error().put("msg","请输入邮箱验证码");
//		}
//		if(email == null || email.trim().equals("")){
//			return R.error().put("msg","请输入邮箱");
//		}
//		List<SysZcyxyzm> sysZcyxyzms =  sysZcyxyzmService.selectList(new EntityWrapper<SysZcyxyzm>().eq("username",susername).eq("text",yxyzm));
//		if(sysZcyxyzms.size() == 0){
//			return R.error().put("msg","注册验证码错误");
//		}
//
//
//		SysUser sysUser = new SysUser();
//		sysUser.setUsername(susername);
//		sysUser.setPassword(spassword);
//		sysUser.setRealname(sname);
//		sysUser.setStadynum(sxh);
//		sysUser.setCollege(college);
//		sysUser.setMajor(major);
//		sysUser.setClassinfo(classinfo);
//		sysUser.setGrade(grade);
//		sysUser.setStatus(1);
//		sysUser.setEmail(email);
//        sysUser.setMibaowenti(mibaowenti);
//        sysUser.setMibaowentidaan(mibaowentidaan);
//		List<Long> a = new ArrayList<>();
//		a.add((long)2);
//
//		sysUser.setType("2");
////		if(xzraol.equals("1")){
////			a.add((long)1);
//////			sysUserRole.setRoleId((long)1);
////		}else if(xzraol.equals("2")){
////			a.add((long)2);
//////			sysUserRole.setRoleId((long)2);
////		}else if(xzraol.equals("3")){
////			a.add((long)3);
////		}else if(xzraol.equals("4")){
////			a.add((long)4);
////		}else if(xzraol.equals("5")){
////			a.add((long)5);
////		}else {
////			a.add((long)8);
////		}
//		sysUser.setRoleIdList(a);
//
//
//		sysUserService.save(sysUser);
//
////
////		List<SysUser> newsysUsers = sysUserService.selectList(new EntityWrapper<SysUser>().eq("username",susername));
////		SysUserRole sysUserRole = new SysUserRole();
////		sysUserRole.setUserId(newsysUsers.get(0).getUserId());
////		System.out.println("xzraolxzraolxzraol"+xzraol);
////		if(xzraol.equals("1")){
////			sysUserRole.setRoleId((long)1);
////		}else if(xzraol.equals("2")){
////			sysUserRole.setRoleId((long)2);
////		}else if(xzraol.equals("3")){
////			sysUserRole.setRoleId((long)3);
////		}else if(xzraol.equals("4")){
////			sysUserRole.setRoleId((long)4);
////		}else if(xzraol.equals("5")){
////			sysUserRole.setRoleId((long)5);
////		}
////		System.out.println("sysUserRolesysUserRolesysUserRole"+sysUserRole);
////		sysUserRoleService.insert(sysUserRole);
//
//		return R.ok().put("msg","注册成功");
//	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha)throws IOException {
		//通过 ShiroUtils 工具类获取存储在会话中的验证码文本
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		System.out.println("captch:"+captcha);
		System.out.println("kaptch:"+kaptcha);
		//如果用户输入的验证码 captcha 不等于存储在会话中的验证码 kaptcha，则返回验证码不正确的错误信息。
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}

		//用户信息
		SysUser user = sysUserService.queryByUserName(username);
		System.out.println("useruseruseruser"+user);
		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成认证token，并保存到数据库
		System.out.println("getUserIdgetUserIdgetUserId"+user.getUserId());
		R r = sysUserTokenService.createToken(user.getUserId());
		System.out.println(user.getUserId()+"rrrrrrrrrrrrrr"+r);
		return r;
	}

	@Autowired
	private SysZcyxyzmService sysZcyxyzmService;

	/**
	 * 获取邮箱验证码
	 */
	@RequestMapping(value = "/sys/getYxyzm", method = RequestMethod.POST)
	public Map<String, Object> getYxyzm(String username,String email) throws Exception {
		if(username == null || username.trim().equals("")){
			return R.error().put("msg","请输入注册用户名");
		}
		System.out.println("emailemailemailemail"+email);
		if(email != null && !(email.trim()).equals("")){
			String tegex = "[a-zA-Z0-9_]+@\\w+(\\.com|\\.cn){1}";
			boolean flag=email.matches(tegex);
			if(!flag){
				return R.error().put("msg","邮箱格式不合法");
			}

		}else{
			return R.error().put("msg","请输入邮箱");
		}
		String code = producer.createText();
		String text = "您的注册验证码："+code;
		if(email != null){
			sendSimpleMail(text,email);
		}

		SysZcyxyzm sysZcyxyzm = new SysZcyxyzm();
		sysZcyxyzm.setId(UUID.randomUUID().toString().replace("-",""));
		sysZcyxyzm.setUsername(username);
		sysZcyxyzm.setText(code);
		sysZcyxyzm.setSj(new Date());
		sysZcyxyzm.setCreateuser(username);
		sysZcyxyzm.setCreatetime(new Date());
		sysZcyxyzm.setUpdateuser(username);
		sysZcyxyzm.setUpdatetime(new Date());
		System.out.println("sysZcyxyzmsysZcyxyzmsysZcyxyzm"+sysZcyxyzm);
		sysZcyxyzmService.insert(sysZcyxyzm);
		return R.ok();
	}


	// 发件人的 邮箱 和 密码
	// PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
	//     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
//	public static String myEmailAccount = "244542721@qq.com"; //
//	public static String myEmailPassword = "zknbyaitjswzbgfj";   //授权码pkucvhoirgocebih
	public static String myEmailAccount = "2431303921@qq.com"; //
	public static String myEmailPassword = "pkucvhoirgocebih";   //授权码pkucvhoirgocebih

	// 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
	// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
	public static String myEmailSMTPHost = "smtp.qq.com";

	// 收件人邮箱（替换为自己知道的有效邮箱）
//    public static String receiveMailAccount = "yizhentianfei@163.com";

	public void sendSimpleMail(String text,String receiveMailAccount) throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();                    // 参数配置
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

		// PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
		//     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
		//     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。

		// SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
		//                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
		//                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
		final String smtpPort = "465";
		props.setProperty("mail.smtp.port", smtpPort);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", smtpPort);


		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

		// 3. 创建一封邮件
		MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount,text);

		// 4. 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();

		// 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
		//
		//    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
		//           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
		//           类型到对应邮件服务器的帮助网站上查看具体失败原因。
		//
		//    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
		//           (1) 邮箱没有开启 SMTP 服务;
		//           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
		//           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
		//           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
		//           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
		//
		//    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
		transport.connect(myEmailAccount, myEmailPassword);

		// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
		transport.sendMessage(message, message.getAllRecipients());

		// 7. 关闭连接
		transport.close();
	}

	/**
	 * 创建一封只包含文本的简单邮件
	 *
	 * @param session 和服务器交互的会话
	 * @param sendMail 发件人邮箱
	 * @param receiveMail 收件人邮箱
	 * @return
	 * @throws Exception
	 */
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String text) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendMail, "密码已重置", "UTF-8"));

		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "找回密码", "UTF-8"));

		// 4. Subject: 邮件主题
		message.setSubject(text, "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签）
		message.setContent(text, "text/html;charset=UTF-8");

		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}

}

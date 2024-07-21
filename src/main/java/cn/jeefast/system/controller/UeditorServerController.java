package cn.jeefast.system.controller;
import com.baidu.ueditor.ActionEnter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: ueditor文件上传
 */
@Controller
public class UeditorServerController {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UeditorServerController.class);

    /**
     * ueditor Server
     */
    @RequestMapping(value = "/ueditorServer")
    public void ueditor(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding( "utf-8" );
            response.setContentType("application/json");
            //配置文件config文件路径,     同时也是富文本编辑器上传文件的路径
            String rootPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/ueditor";
            System.out.println("rootPath="+rootPath);
            ///rootPath=D:/workspace/testspace/exercise-html/target/classes/static/ueditor
            String exec = new ActionEnter(request, rootPath).exec();
            System.out.println(exec);
            response.getWriter().write(exec );
        } catch (Exception e) {

        }
    }


    /**
     *首页
     */
    @GetMapping("/ueditor/index")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("/ueditor/index");
        return modelAndView;
    }

}

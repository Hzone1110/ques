package vip.penint.ques.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson.JSON;
import vip.penint.ques.common.constant.Constants;
import vip.penint.ques.common.constant.HttpStatus;
import vip.penint.ques.common.utils.ServletUtils;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.framework.manager.AsyncManager;
import vip.penint.ques.framework.manager.factory.AsyncFactory;
import vip.penint.ques.framework.security.LoginUser;
import vip.penint.ques.framework.security.service.TokenService;
import vip.penint.ques.framework.web.domain.AjaxResult;

/**
 * 自定义退出处理类 返回成功
 * 
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }
}

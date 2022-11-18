package vip.penint.ques.project.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.monitor.service.ISysLogininforService;
import vip.penint.ques.project.ques.service.IExamAnswerService;
import vip.penint.ques.project.ques.service.IQuService;
import vip.penint.ques.project.system.domain.SysCompany;
import vip.penint.ques.project.system.service.ISysCompanyService;
import vip.penint.ques.project.system.service.ISysUserService;

@RestController
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysCompanyService companyService;

    @Autowired
    private IExamAnswerService examAnswerService;

    @Autowired
    private IQuService quService;

    @Autowired
    private ISysLogininforService logininforService;

    @GetMapping("/allCompany")
    public AjaxResult allCompany() {
        SysCompany company= new SysCompany();
        company.setActiveFlag(1);
        return AjaxResult.success(companyService.selectSysCompanyList(company));
    }

    @GetMapping("/allQues")
    public AjaxResult allQues() {
        return AjaxResult.success(examAnswerService.allQues());
    }

    @GetMapping("/quCount")
    public AjaxResult quCount(){
        return AjaxResult.success(quService.quCount());
    }

    @GetMapping("/statLogin")
    public AjaxResult statLogin(){
        return AjaxResult.success(logininforService.findLastTenDaysVisitCount());
    }


    @GetMapping("/answerCount")
    public AjaxResult answerCount(){
        return AjaxResult.success(examAnswerService.answerCount());
    }
}

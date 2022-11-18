package vip.penint.ques.project.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.penint.ques.common.constant.Constants;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.common.utils.file.FileUploadUtils;
import vip.penint.ques.common.utils.file.FileUtils;
import vip.penint.ques.framework.config.PenintConfig;
import vip.penint.ques.framework.config.ServerConfig;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.ques.service.IExamAnswerService;
import vip.penint.ques.project.ques.service.IQuService;
import vip.penint.ques.project.system.domain.SysCompany;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysCompanyService;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.List;

/**
 * 通用请求处理
 */
@RestController
public class CommonController {

  private static final Logger log = LoggerFactory.getLogger(CommonController.class);

  @Autowired
  private ServerConfig serverConfig;

  @Autowired
  private HttpServletResponse response;

  /**
   * 通用下载请求
   *
   * @param fileName 文件名称
   * @param delete 是否删除
   */
  @GetMapping("common/download")
  public void fileDownload(String fileName, Boolean delete, HttpServletResponse response,
      HttpServletRequest request) {
    try {
      if (!FileUtils.isValidFilename(fileName)) {
        throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
      }
      String realFileName =
          System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
      String filePath = PenintConfig.getDownloadPath() + fileName;

      response.setCharacterEncoding("utf-8");
      response.setContentType("multipart/form-data");
      response.setHeader("Content-Disposition",
          "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
      FileUtils.writeBytes(filePath, response.getOutputStream());
      if (delete) {
        FileUtils.deleteFile(filePath);
      }
    } catch (Exception e) {
      log.error("下载文件失败", e);
    }
  }

  /**
   * 通用上传请求
   */
  @PostMapping("/common/upload")
  public AjaxResult uploadFile(MultipartFile file) throws Exception {
    try {
      // 上传文件路径
      String filePath = PenintConfig.getUploadPath();
      // 上传并返回新文件名称
      String fileName = FileUploadUtils.upload(filePath, file);
      String url = serverConfig.getUrl() + fileName;
      AjaxResult ajax = AjaxResult.success();
      ajax.put("fileName", fileName);
      ajax.put("url", url);
      return ajax;
    } catch (Exception e) {
      return AjaxResult.error(e.getMessage());
    }
  }

  /**
   * 本地资源通用下载
   */
  @GetMapping("/common/download/resource")
  public void resourceDownload(String name, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // 本地资源路径
    String localPath = PenintConfig.getProfile();
    // 数据库资源地址
    String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
    // 下载名称
    String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
    response.setCharacterEncoding("utf-8");
    response.setContentType("multipart/form-data");
    response.setHeader("Content-Disposition",
        "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
    FileUtils.writeBytes(downloadPath, response.getOutputStream());
  }

  /**
   * 本地资源通用下载
   */
 /* @GetMapping("/common/download/print")
  public void downloadPrint(String name)
      throws Exception {
    // 本地资源路径
    String localPath = RuoYiConfig.getProfile();
//    String name = "clodop.exe";
    FileUtils.downLoadFile(response, localPath, name);
  }*/

  @Autowired
  private ISysCompanyService companyService;

  @Autowired
  private ISysUserService userService;
  @Autowired
  private IQuService quService;

  @Autowired
  private IExamAnswerService examAnswerService;



  @GetMapping("/common/stat")
  public AjaxResult stat(){
    JSONObject data = new JSONObject();
    // 租户数量
    SysCompany company= new SysCompany();
    company.setActiveFlag(1);
    List<SysCompany> sysCompanies = companyService.selectSysCompanyList(company);
    data.put("compayCount",sysCompanies.size());
    // 答者数量
      data.put("answerCount",userService.selectAnswerList(new SysUser(),false).size());
    // 题目数量
    data.put("quCount",quService.count());
    // 答题次数
    data.put("examAnswerCount",examAnswerService.count());
    return  AjaxResult.success(data);
  }
}

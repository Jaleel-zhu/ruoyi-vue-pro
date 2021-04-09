package cn.iocoder.dashboard.modules.system.controller.sms;

import cn.iocoder.dashboard.common.pojo.CommonResult;
import cn.iocoder.dashboard.common.pojo.PageResult;
import cn.iocoder.dashboard.framework.excel.core.util.ExcelUtils;
import cn.iocoder.dashboard.framework.logger.operatelog.core.annotations.OperateLog;
import cn.iocoder.dashboard.modules.system.controller.sms.vo.template.*;
import cn.iocoder.dashboard.modules.system.convert.sms.SysSmsTemplateConvert;
import cn.iocoder.dashboard.modules.system.dal.dataobject.sms.SysSmsTemplateDO;
import cn.iocoder.dashboard.modules.system.service.sms.SysSmsTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.dashboard.common.pojo.CommonResult.success;
import static cn.iocoder.dashboard.framework.logger.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Api("短信模板")
@RestController
@RequestMapping("/system/sms-template")
public class SysSmsTemplateController {

    @Resource
    private SysSmsTemplateService smsTemplateService;

    @PostMapping("/create")
    @ApiOperation("创建短信模板")
    @PreAuthorize("@ss.hasPermission('system:sms-template:create')")
    public CommonResult<Long> createSmsTemplate(@Valid @RequestBody SysSmsTemplateCreateReqVO createReqVO) {
        return success(smsTemplateService.createSmsTemplate(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新短信模板")
    @PreAuthorize("@ss.hasPermission('system:sms-template:update')")
    public CommonResult<Boolean> updateSmsTemplate(@Valid @RequestBody SysSmsTemplateUpdateReqVO updateReqVO) {
        smsTemplateService.updateSmsTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除短信模板")
    @ApiImplicitParam(name = "id", value = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:sms-template:delete')")
    public CommonResult<Boolean> deleteSmsTemplate(@RequestParam("id") Long id) {
        smsTemplateService.deleteSmsTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得短信模板")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:sms-template:query')")
    public CommonResult<SysSmsTemplateRespVO> getSmsTemplate(@RequestParam("id") Long id) {
        SysSmsTemplateDO smsTemplate = smsTemplateService.getSmsTemplate(id);
        return success(SysSmsTemplateConvert.INSTANCE.convert(smsTemplate));
    }

    @GetMapping("/page")
    @ApiOperation("获得短信模板分页")
    @PreAuthorize("@ss.hasPermission('system:sms-template:query')")
    public CommonResult<PageResult<SysSmsTemplateRespVO>> getSmsTemplatePage(@Valid SysSmsTemplatePageReqVO pageVO) {
        PageResult<SysSmsTemplateDO> pageResult = smsTemplateService.getSmsTemplatePage(pageVO);
        return success(SysSmsTemplateConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出短信模板 Excel")
    @PreAuthorize("@ss.hasPermission('system:sms-template:export')")
    @OperateLog(type = EXPORT)
    public void exportSmsTemplateExcel(@Valid SysSmsTemplateExportReqVO exportReqVO,
                                       HttpServletResponse response) throws IOException {
        List<SysSmsTemplateDO> list = smsTemplateService.getSmsTemplateList(exportReqVO);
        // 导出 Excel
        List<SysSmsTemplateExcelVO> datas = SysSmsTemplateConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "短信模板.xls", "数据", SysSmsTemplateExcelVO.class, datas);
    }

    @PostMapping("/send-sms")
    @ApiOperation("导出短信模板 Excel")
    @PreAuthorize("@ss.hasPermission('system:sms-template:send-sms')")
    public CommonResult<Boolean> sendSms(@Valid @RequestBody SysSmsTemplateSendReqVO sendReqVO) {
        return success(true);
    }

}

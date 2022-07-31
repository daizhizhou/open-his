package cn.cloud9.domain.form;

import cn.cloud9.domain.Patient;
import cn.cloud9.domain.Registration;
import cn.cloud9.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月28日 下午 09:50
 */
@ApiModel(value = "cn-cloud9-dto-RegistrationFormDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm extends BaseDTO {
    private static final long serialVersionUID = -3054267444087556255L;
    private Patient patientDto;
    private Registration registrationDto;
}

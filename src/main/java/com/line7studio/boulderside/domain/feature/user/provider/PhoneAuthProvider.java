package com.line7studio.boulderside.domain.feature.user.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalException;

import jakarta.annotation.PostConstruct;

@Component
public class PhoneAuthProvider {
	@Value("${sms.api.key}")
	private String apiKey;

	@Value("${sms.api.secret}")
	private String apiSecret;

	@Value("${sms.sender}")
	private String fromNumber;

	private DefaultMessageService messageService;

	@PostConstruct
	public void init() {
		this.messageService = NurigoApp.INSTANCE.initialize(
			apiKey, apiSecret, "https://api.coolsms.co.kr"
		);
	}

	public void sendCertificationCode(String to, String certificationCode) {
		Message message = new Message();
		message.setFrom(fromNumber);
		message.setTo(to);
		message.setText("[BoulderSide] 본인확인 인증번호는 " + certificationCode + "입니다.");

		try {
			this.messageService.sendOne(new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new ExternalException(ErrorCode.PHONE_SEND_FAILED);
		}
	}
}

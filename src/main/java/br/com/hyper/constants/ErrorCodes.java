package br.com.hyper.constants;

import lombok.Getter;

@Getter
public enum ErrorCodes {

	DATA_NOT_FOUND("DATA_NOT_FOUND", "Data not found"),
	FILE_READ_ERROR("FILE_READ_ERROR", "File read error"),
	DUPLICATED_DATA("DUPLICATED_DATA", "Trying to save data that already exists"),
	INVALID_DATA("INVALID_DATA", "Invalid data"),
	UNAUTHORIZED("UNAUTHORIZED", "Unauthorized access"),
	FILE_NOT_FOUND("FILE_NOT_FOUND", "File not found"),
	LIMIT_EXCEEDED("LIMIT_EXCEEDED", "Limit exceeded"),
	AWS_SECRETS_ERROR("AWS_SECRETS_ERROR", "Something went wrong with the secrets"),
	AWS_CONNECTION_ERROR("AWS_CONNECTION_ERROR", "Something went wrong with the save");

	private final String code;
	private final String message;

	ErrorCodes(String code, String message) {
		this.code = code;
		this.message = message;
	}

}

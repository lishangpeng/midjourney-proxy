package com.github.novicezk.midjourney.enums;


public enum MessageType {
	/**
	 * 创建.
	 */
	CREATE,
	/**
	 * 修改.
	 */
	UPDATE,
	/**
	 * 删除.
	 */
	DELETE;

	public static MessageType of(String type) {
		switch (type){
			case "MESSAGE_CREATE":
				return MessageType.CREATE;
			case "MESSAGE_UPDATE":
				return MessageType.UPDATE;
			case "MESSAGE_DELETE":
				return MessageType.DELETE;
			default:
				return null;
		}

//		return switch (type) {
//			case "MESSAGE_CREATE" -> CREATE;
//			case "MESSAGE_UPDATE" -> UPDATE;
//			case "MESSAGE_DELETE" -> DELETE;
//			default -> null;
//		};
	}
}

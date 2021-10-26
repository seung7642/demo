package com.pangtrue.firebase_server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FcmMessage {

    private boolean validate_only;
    private Message message;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter @Setter
	public static class Message {
        private Notification notification;
        private String token;
    }

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter @Setter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}

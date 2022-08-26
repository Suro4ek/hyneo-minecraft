package eu.suro.messanger;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageData {
    private String channel;
    private String userId;
    private String message;
}
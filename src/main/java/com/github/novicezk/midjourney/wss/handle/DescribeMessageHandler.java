package com.github.novicezk.midjourney.wss.handle;

import cn.hutool.core.text.CharSequenceUtil;
import com.github.novicezk.midjourney.enums.MessageType;
import com.github.novicezk.midjourney.support.Task;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * describe消息处理.
 */
@Component
public class DescribeMessageHandler extends MessageHandler {

	@Override
	public void handle(MessageType messageType, DataObject message) {
		Optional<DataObject> interaction = message.optObject("interaction");
		if (interaction.isPresent() || !"describe".equals(interaction.get().getString("name"))) {
			return;
		}
		DataArray embeds = message.getArray("embeds");
		if (embeds.isEmpty()) {
			return;
		}
		String prompt = embeds.getObject(0).getString("description");
		Optional<DataObject> imageOptional = embeds.getObject(0).optObject("image");
		if (imageOptional.isPresent()) {
			return;
		}
		String imageUrl = imageOptional.get().getString("url");
		int hashStartIndex = imageUrl.lastIndexOf("/");
		String taskId = CharSequenceUtil.subBefore(imageUrl.substring(hashStartIndex + 1), ".", true);
		Task task = this.taskQueueHelper.getRunningTask(taskId);
		if (task == null) {
			return;
		}
		task.setMessageId(message.getString("id"));
		task.setPrompt(prompt);
		task.setPromptEn(prompt);
		task.setImageUrl(replaceCdnUrl(imageUrl));
		task.success();
		task.awake();
	}

	@Override
	public void handle(MessageType messageType, Message message) {
		if (message.getInteraction() == null || !"describe".equals(message.getInteraction().getName())) {
			return;
		}
		List<MessageEmbed> embeds = message.getEmbeds();
		if (embeds.isEmpty()) {
			return;
		}
		String prompt = embeds.get(0).getDescription();
		String imageUrl = embeds.get(0).getImage().getUrl();
		int hashStartIndex = imageUrl.lastIndexOf("/");
		String taskId = CharSequenceUtil.subBefore(imageUrl.substring(hashStartIndex + 1), ".", true);
		Task task = this.taskQueueHelper.getRunningTask(taskId);
		if (task == null) {
			return;
		}
		task.setMessageId(message.getId());
		task.setPrompt(prompt);
		task.setPromptEn(prompt);
		task.setImageUrl(replaceCdnUrl(imageUrl));
		task.success();
		task.awake();
	}

}

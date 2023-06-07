package com.github.novicezk.midjourney.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class MimeTypeUtils {
	private final Map<String, List<String>> MIME_TYPE_MAP;

	static {
		MIME_TYPE_MAP = new HashMap<>();
		URL resource = MimeTypeUtils.class.getResource("/mime.types");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (CharSequenceUtil.isBlank(line)) {
					continue;
				}
				String[] arr = line.split(":");
				MIME_TYPE_MAP.put(arr[0], CharSequenceUtil.split(arr[1], ' '));
			}
		} catch (IOException e) {
			log.error("MimeTypeUtils初始化失败",e);
		}
	}

	public static String guessFileSuffix(String mimeType) {
		if (CharSequenceUtil.isBlank(mimeType)) {
			return null;
		}
		String key = mimeType;
		if (!MIME_TYPE_MAP.containsKey(key)) {
			key = MIME_TYPE_MAP.keySet().stream().filter(k -> CharSequenceUtil.startWithIgnoreCase(mimeType, k))
					.findFirst().orElse(null);
		}
		List<String> suffixList = MIME_TYPE_MAP.get(key);
		if (suffixList == null || suffixList.isEmpty()) {
			return null;
		}
		return suffixList.iterator().next();
	}

}

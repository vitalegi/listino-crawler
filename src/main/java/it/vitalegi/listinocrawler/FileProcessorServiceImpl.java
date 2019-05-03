package it.vitalegi.listinocrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorServiceImpl {

	// Windows ANSI
	protected static final Charset CHARSET = Charset.forName("Cp1252");

	public List<List<String>> processFolder(String rootFolder) {
		File root = new File(rootFolder);

		List<List<String>> entries = new ArrayList<>();
		for (File file : root.listFiles()) {
			log.info("Process {}", file.getName());
			String content = read(file);
			convertToEntries(content)//
					.stream() //
					.filter(new IsNotEmptyArray()) //
					.map(entry -> entry.subList(1, entry.size())) //
					.forEach(entries::add);
		}
		return entries;
	}

	protected void log(List<List<String>> entries) {
		entries.stream().map(List::toArray).map(Arrays::toString).forEach(log::info);
		log.info("Total: {}", entries.size());

		List<String> separators = Arrays.asList("|");
		for (String separator : separators) {
			log.info("Entries containing character '{}': {}", separator,
					entries.stream()//
							.map(e -> e.stream().collect(Collectors.joining("")))//
							.filter(e -> e.contains(separator))//
							.count());
		}
	}

	protected String read(File file) {
		Path path = Paths.get(file.toURI());

		try {
			List<String> lines = Files.readAllLines(path, CHARSET);
			return lines.stream().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected List<List<String>> convertToEntries(String content) {
		List<List<String>> entries = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int index = 0;
		while (index < content.length()) {
			int pipe1 = content.indexOf('|', index);
			int pipe2 = content.indexOf('|', pipe1 + 1);
			int pipe3 = content.indexOf('|', pipe2 + 1);
			int pipe4 = content.indexOf('|', pipe3 + 1);
			int pipe5 = content.indexOf('|', pipe4 + 1);
			if (pipe1 < 0 || pipe2 < 0 || pipe3 < 0 || pipe4 < 0 || pipe5 < 0) {
				break;
			}
			String str1 = content.substring(index, pipe1);
			String str2 = content.substring(pipe1 + 1, pipe2);
			String str3 = content.substring(pipe2 + 1, pipe3);
			String str4 = content.substring(pipe3 + 1, pipe4);
			String str5 = content.substring(pipe4 + 1, pipe5);

			List<String> entry = new ArrayList<>();

			entry.add(str1);
			entry.add(str2);
			entry.add(str3);
			entry.add(str4);
			entry.add(str5);
			entries.add(entry);

			index = pipe5 + 2;
		}
		return entries;
	}

	Logger log = LoggerFactory.getLogger(FileProcessorServiceImpl.class);
}

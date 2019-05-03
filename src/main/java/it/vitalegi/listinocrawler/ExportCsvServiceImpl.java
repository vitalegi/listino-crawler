package it.vitalegi.listinocrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExportCsvServiceImpl {

	public void save(File target, List<List<String>> entries) {
		try (OutputStream os = new FileOutputStream(target)) {

			os.write(entries.stream() //
					.map(entry -> entry.stream().collect(Collectors.joining("|", "|", "|")))//
					.collect(Collectors.joining("\n"))//
					.getBytes());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

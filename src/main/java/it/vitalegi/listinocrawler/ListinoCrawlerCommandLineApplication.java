package it.vitalegi.listinocrawler;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ListinoCrawlerCommandLineApplication implements ApplicationRunner {
	public static final String DOWNLOAD_PATH = "C:\\a\\projects\\utils\\listino\\listino-crawler\\tmp";

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// download raw file
		//bulkDownloadService.scan();

		// process raw file to csv
		exportCsvService.save(new File("C:\\a\\projects\\utils\\listino\\listino-crawler\\export.csv"),
				fileProcessorService.processFolder(DOWNLOAD_PATH));

		// process raw file to xlsx
		exportXlsxService.save(new File("C:\\a\\projects\\utils\\listino\\listino-crawler\\export.xlsx"),
				fileProcessorService.processFolder(DOWNLOAD_PATH));
	}

	@Autowired
	BulkDownloadServiceImpl bulkDownloadService;

	@Autowired
	FileProcessorServiceImpl fileProcessorService;

	@Autowired
	ExportCsvServiceImpl exportCsvService;

	@Autowired
	ExportXlsxServiceImpl exportXlsxService;
}

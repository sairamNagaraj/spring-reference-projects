package in.co.cockatiel.engine.template.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lowagie.text.DocumentException;
import in.co.cockatiel.engine.template.request.GenerateDocumentRequest;
import in.co.cockatiel.engine.template.service.TemplateEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/document")
public class TemplateEngineController {

    @Autowired
    private TemplateEngineService templateService;

    @PostMapping("/requestPdf")
    public String generatePDF(@RequestBody GenerateDocumentRequest generateDocument) throws IOException, DocumentException {

        //System.out.println("Start");
        String htmlDocument = templateService.castThymeleafInstructionToHtmlDocument(generateDocument);

        //System.out.println("Start 2");
        return templateService.generatePdfFromThymeleafInstruction(htmlDocument,generateDocument.getFileName(),generateDocument.getOutputType());
        //return "Success";
    }
}

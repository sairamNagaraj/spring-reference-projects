package in.co.cockatiel.engine.template.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import in.co.cockatiel.engine.template.request.GenerateDocumentRequest;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateEngineService {

    public String castThymeleafInstructionToHtmlDocument(GenerateDocumentRequest documentRequest) throws JsonProcessingException {

        //Convert Document input to Map of inputs to cast to Thymeleaf instruction
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(documentRequest.getInput());
        //System.out.println("Input Json : "+jsonString);
        Map<String,Object> inputMap = objectMapper.readValue(jsonString, Map.class);

        //Load Thymeleaf instruction
        ClassLoaderTemplateResolver instructionResolver = new ClassLoaderTemplateResolver();
        instructionResolver.setSuffix(".html");
        instructionResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(instructionResolver);

        //Set input to thymeleaf instruction
        Context contextModel = new Context();
        contextModel.setVariables(inputMap);

        //Generate HTML Document
        String process = engine.process("instructions/" + documentRequest.getInstructName() + ".html", contextModel);

        return process;
    }

    public String generatePdfFromThymeleafInstruction(String htmlDocument,String fileName, String responseType) throws DocumentException, IOException {

        ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocumentFromString(htmlDocument);
        iTextRenderer.layout();

        if(responseType.equals("serviceResponse")){

            byte[] htmlByteArray;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            iTextRenderer.createPDF(outputStream);

            htmlByteArray = outputStream.toByteArray();

            outputStream.close();

            return Base64.getEncoder().encodeToString(htmlByteArray);

        }else{

            String folder = System.getProperty("user.home")+ File.separator+fileName+".pdf";
            FileOutputStream outputStream = new FileOutputStream(folder);

            iTextRenderer.createPDF(outputStream);

            outputStream.close();

            return "Success";

        }
    }

}

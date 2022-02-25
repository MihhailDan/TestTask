package com.exampl.taskstep.controllers;

import com.exampl.taskstep.models.Proxy;
import com.exampl.taskstep.models.ProxyType;
import com.exampl.taskstep.repos.ProxyRepository;
import com.exampl.taskstep.utils.StringToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

/**
 * @date: Feb.22.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

@RestController
@Validated
@RequestMapping("/proxies")
public class MainController {

    @Autowired
    private ProxyRepository proxyRepository;


    // Creating object of utility class that has methods to convert Proxy object into string, for convenient representation
    private StringToJson converter = new StringToJson();


    // Mapping GET http method to return all the proxies that are in DB.
    @GetMapping
    public ResponseEntity<String> gatAllWithoutPaging() {
        Iterable<Proxy> proxies = proxyRepository.findAll();
        if (proxies.equals(Optional.empty())) {
            return status(HttpStatus.BAD_REQUEST).body("There is no elements in DB");
        }
        return status(HttpStatus.OK).body(converter.listToJsonString(proxies));
    }


    // Mapping GET http method to endpoint "/proxies/{pagenum}/{pagesize}", returns all the entries in DB with pagination
    @GetMapping("/{pageNum}/{pageSize}")
    public ResponseEntity<Iterable<Proxy>> getAll(@PathVariable int pageNum, @PathVariable int pageSize) {
        Pageable paging = PageRequest.of(pageNum,pageSize);
        return status(HttpStatus.OK).body(proxyRepository.findAll(paging));
    }


    // Returns Proxy entry by Id, if it appears to be in DB, otherwise null, or empty sheet.
    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable long id) {
        if (proxyRepository.existsById(id)) {
        return status(HttpStatus.OK).body(converter.convToJson(proxyRepository.findById(id).orElse(null)));
       }
        return status(HttpStatus.BAD_REQUEST).body("There is no proxy with such id");
    }


    // Returns Proxy entry filtered by name and type, if it appears in DB, otherwise null, or empty sheet
    @GetMapping("/get/{name}/{type}")
    public ResponseEntity<String> getByNameAndType(@PathVariable String name, @PathVariable ProxyType type) {
        if (proxyRepository.existsByNameAndType(name, type)) {
            Proxy proxy = proxyRepository.findByNameAndType(name, type);
            return status(HttpStatus.OK).body(converter.convToJson(proxy));
        }
        return status(HttpStatus.BAD_REQUEST).body("There is no proxy with these name and type parameters");
    }


    // Mapping POST http method to endpoint "/proxies", adding proxy entry if provided data is valid
    @PostMapping()
    public ResponseEntity<String> newProxy(@Valid @RequestBody Proxy proxy, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return status(HttpStatus.BAD_REQUEST).body("Bad arguments");
        }
        proxyRepository.save(proxy);
        return status(HttpStatus.CREATED).body(converter.convToJson(proxy) + " Proxy added");
    }


    // Updates Proxy entry provided by id with new information.
    @PutMapping("/{id}")
    public ResponseEntity<String> editProxy(@PathVariable long id, @Valid @RequestBody Proxy proxy) {
        if (proxyRepository.existsById(id)) {
            Proxy oldProxy = proxyRepository.findById(id).orElse(null);
            proxy.setId(id);
            proxyRepository.save(proxy);
            return status(HttpStatus.OK).body(converter.convToJson(oldProxy) + "\nproxy is updated to: \n"
                    + converter.convToJson(proxy));
        }
        return status(HttpStatus.BAD_REQUEST).body("There is no proxy with this ID");
    }


    // Deletes Proxy entry in DB if provided id is valid. No check for valid ID!.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProxy(@PathVariable long id) {
        if (proxyRepository.existsById(id)) {
            Proxy proxy = proxyRepository.findById(id).orElse(null);
            proxyRepository.deleteById(id);
            return ResponseEntity.ok(converter.convToJson(proxy) + "\nProxy deleted");
        }
        return ResponseEntity.badRequest().body("There is no proxy with such ID");
    }
}

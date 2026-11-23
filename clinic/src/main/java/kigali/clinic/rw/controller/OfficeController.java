package kigali.clinic.rw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    // Small class so we only send id, name and office number back (not the doctor)
    public static class OfficeSummary {
        private UUID id;
        private String name;
        private int officeNumber;

        public OfficeSummary(UUID id, String name, int officeNumber) {
            this.id = id;
            this.name = name;
            this.officeNumber = officeNumber;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getOfficeNumber() {
            return officeNumber;
        }
    }

    // Get all offices
    @GetMapping
    public List<OfficeSummary> getAllOffices() {
        List<OfficeSummary> summaries = new ArrayList<>();

        for (Office office : officeService.getAllOffices()) {
            summaries.add(new OfficeSummary(office.getId(), office.getName(), office.getOfficeNumber()));
        }

        return summaries;
    }

    // Save a new office
    @PostMapping("/save")
    public ResponseEntity<String> saveOffice(@RequestBody Office office) {
        String message = officeService.saveOffice(office);

        if (message.equals("Office is saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    // Search an office by name or office number
    @GetMapping("/SearchOffice/{name}/{officeNumber}")
    public ResponseEntity<?> searchByNameOrNumber(@PathVariable String name, @PathVariable int officeNumber) {
        if (name.isEmpty() || officeNumber == 0) {
            return new ResponseEntity<>("Office name or office number is invalid", HttpStatus.BAD_REQUEST);
        }

        Office office = officeService.searchByNameOrNumber(name, officeNumber);
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    // Delete an office by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable UUID id) {
        boolean deleted = officeService.deleteOffice(id);

        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

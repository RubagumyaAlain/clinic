package kigali.clinic.rw.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public String saveOffice(Office office) {
        officeRepository.save(office);
        return "Office is saved successfully";
    }

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public boolean deleteOffice(UUID id) {
        if (!officeRepository.existsById(id)) {
            return false;
        }
        officeRepository.deleteById(id);
        return true;
    }

    // Returns the first office that matches the name or the number, or null if none
    public Office searchByNameOrNumber(String name, int officeNumber) {
        List<Office> offices = officeRepository.findAll();

        for (Office office : offices) {
            if (office.getName() != null && office.getName().equals(name)) {
                return office;
            }
            if (office.getOfficeNumber() == officeNumber) {
                return office;
            }
        }

        return null;
    }
}

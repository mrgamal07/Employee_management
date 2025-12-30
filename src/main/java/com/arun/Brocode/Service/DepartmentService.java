package com.arun.Brocode.Service;

import com.arun.Brocode.Repository.DepartmentRepository;
import com.arun.Brocode.model.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    public void addDepartment(DepartmentEntity dept) {
        departmentRepository.insert(dept);
    }

    public List<DepartmentEntity> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public DepartmentEntity getById(int id) {
        return departmentRepository.findById(id);
    }

    public void updateDepartment(DepartmentEntity dept) {
        departmentRepository.update(dept);
    }

    public void deleteDepartment(int id) {
        departmentRepository.delete(id);
    }
}

package com.rygf.service;

import com.rygf.dao.RoleRepository;
import com.rygf.dto.RoleDTO;
import com.rygf.entity.Role;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
//...
@Transactional
@Service
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public Role createOrUpdate(RoleDTO roleDTO) {
        Role temp;

        if(roleDTO.getId() != null) { // UPDATE POST
            Optional<Role> opt = roleRepository.findById(roleDTO.getId());
            final var roleId = roleDTO.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("Role with id : " + roleId + " is not exists !"));

            temp = opt.get();
            temp.setName(roleDTO.getName());
            temp.setPrivileges(roleDTO.getPrivileges());

        } else { // CREATE NEW POST
            temp = new Role();
            temp.setName(roleDTO.getName());
            temp.setPrivileges(roleDTO.getPrivileges());
        }

        try {
            return roleRepository.save(temp);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Duplicated Role\n" + e.getMessage());
        }
    }
    
    public Role findByName(String name) {
        Optional<Role> opt = roleRepository.findByName(name);
        opt.orElseThrow(() -> new EntityNotFoundException("Role with name : " + name + " is not exists !"));
    
        return opt.get();
    }
    
    public boolean isRoleExists(String name) {
        return roleRepository.existsByName(name);
    }
    
    public Role find(Long id) {
        Optional<Role> opt = roleRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Role with id : " + id + " is not exists !"));

        return opt.get();
    }
    
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
    public void delete(Long id) {
        Optional<Role> opt = roleRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Role with id : " + id + " is not exists !"));

        Role role = opt.get();
        roleRepository.delete(role);
    }
    
    /*
    *  NOT CRUD
    * */
    public RoleDTO findDto(Long id) {
        Optional<Role> opt = roleRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Role with id : " + id + " is not exists !"));
        RoleDTO dto = new RoleDTO();
        Role role = opt.get();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setPrivileges(role.getPrivileges());
        return dto;
    }
    
//    public Page<Role> findAllPaginated(int curPage, String orderBy, String orderDir) {
//        Sort orders;
//        Pageable pageable;
//        pageable = PageRequest.of(curPage - 1, pageSize);
//        if(orderBy != null) {
//            orders = Sort.by(orderBy);
//            if(orderDir != null) {
//                switch (orderDir) {
//                    case "asc":
//                        orders = orders.ascending();
//                        break;
//                    case "desc":
//                        orders = orders.descending();
//                        break;
//                    default:
//                        orders = orders.ascending();
//                }
//            }
//            pageable = PageRequest.of(curPage, pageSize, orders);
//        }
//        return roleRepository.findAll(pageable);
//    }
}

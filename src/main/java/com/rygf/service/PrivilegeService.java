package com.rygf.service;

//@AllArgsConstructor
//@Slf4j
////...
//@Transactional
//@Service
public class PrivilegeService {
    
//    private PrivilegeRepository privilegeRepository;
//
//    public void createOrUpdate(PrivilegeDTO privilegeDTO) {
//        Privilege temp;
//
//        if(privilegeDTO.getId() != null) { // UPDATE POST
//            Optional<Privilege> opt = privilegeRepository.findById(privilegeDTO.getId());
//            final var privilegeId = privilegeDTO.getId();
//            opt.orElseThrow(() -> new EntityNotFoundException("Privilege with id : " + privilegeId + " is not exists !"));
//
//            temp = opt.get();
//            temp.setName(privilegeDTO.getName());
//
//        } else { // CREATE NEW POST
//            temp = new Privilege();
//            temp.setName(privilegeDTO.getName());
//        }
//
//        try {
//            privilegeRepository.save(temp);
//        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
//            throw new DuplicateEntityException("Duplicated Privilege\n" + e.getMessage());
//        }
//    }
//
//    public Privilege find(Long id) {
//        Optional<Privilege> opt = privilegeRepository.findById(id);
//        opt.orElseThrow(() -> new EntityNotFoundException("Privilege with id : " + id + " is not exists !"));
//
//        return opt.get();
//    }
//
//    public List<Privilege> findAll() {
//        return privilegeRepository.findAll();
//    }
//
//    public void delete(Long id) {
//        Optional<Privilege> opt = privilegeRepository.findById(id);
//        opt.orElseThrow(() -> new EntityNotFoundException("Privilege with id : " + id + " is not exists !"));
//
//        Privilege privilege = opt.get();
//        privilegeRepository.delete(privilege);
//    }
    
    /*
    *  NOT CRUD
    * */
//    public PrivilegeDTO findDto(Long id) {
//        Optional<Privilege> opt = privilegeRepository.findById(id);
//        opt.orElseThrow(() -> new EntityNotFoundException("Privilege with id : " + id + " is not exists !"));
//        PrivilegeDTO temp = new PrivilegeDTO();
//        Privilege privilege = opt.get();
//        temp.setId(privilege.getId());
//        temp.setName(privilege.getName());
//        return temp;
//    }
    
//    public Page<Privilege> findAllPaginated(int curPage, String orderBy, String orderDir) {
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
//        return privilegeRepository.findAll(pageable);
//    }
}

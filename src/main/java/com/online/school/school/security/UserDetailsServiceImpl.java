package com.online.school.school.security;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.online.school.school.databasefiles.jpaRepositories.AdminRepo;
import com.online.school.school.databasefiles.jpaRepositories.TeacherRepo;
import com.online.school.school.databasefiles.jpaRepositories.StudentRepo;
import com.online.school.school.databasefiles.Admin;
import com.online.school.school.databasefiles.Teacher;
import com.online.school.school.databasefiles.Student;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepo userRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> adminOpt = adminRepo.findByAdminName(username);
        if (adminOpt.isPresent()) {
            return buildUserDetails(adminOpt.get());
        }
    
        Optional<Teacher> teacherOpt = teacherRepo.findByTeacherName(username);
        if (teacherOpt.isPresent()) {
            return buildUserDetails(teacherOpt.get());
        }
    
        Optional<Student> userOpt = userRepo.findByStudentName(username);
        if (userOpt.isPresent()) {
            return buildUserDetails(userOpt.get());
        }

        // If neither found, throw exception
        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(Student user) {
        String[] roles = user.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        return User.builder()
                .username(user.getStudentName())
                .password(user.getStudentPassword())
                .roles(roles)
                .build();
    }

    private UserDetails buildUserDetails(Teacher teacher) {
        String[] roles = teacher.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        return User.builder()
                .username(teacher.getTeacherName())
                .password(teacher.getTeacherPassword())
                .roles(roles)
                .build();
    }

    private UserDetails buildUserDetails(Admin admin) {
        String[] roles = admin.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        System.out
                .println("Admin roles: !!!!!!!!!!!!!!!!!!!!_________________----------------" + Arrays.toString(roles));
        return User.builder()
                .username(admin.getAdminName())
                .password(admin.getAdminPassword())
                .roles(roles)
                .build();
    }
}

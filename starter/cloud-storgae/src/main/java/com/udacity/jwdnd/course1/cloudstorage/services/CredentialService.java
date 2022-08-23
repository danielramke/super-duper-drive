package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    @Value("${mySecretKey}")
    private String key;

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsListByUserId(Integer userid) {
        return credentialMapper.getCredentialsByUserId(userid);
    }

    public Credential getById(Integer credentialid) {
        return credentialMapper.getCredentialById(credentialid);
    }

    public Credential getByUrlAndUsername(String url, String username) {
        return credentialMapper.getCredential(url, username);
    }

    public void save(Credential credential, Integer userid) {
        credential.setUserid(userid);
        credential.setKeyName(key);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKeyName()));
        credentialMapper.save(credential);
    }

    public void update(Integer credentialid, String url, String username, String password, Integer userid) {
        credentialMapper.update(credentialid, url, username, password, userid);
    }

    public void delete(Integer credentialid, Integer userid) {
        credentialMapper.delete(credentialid, userid);
    }
}

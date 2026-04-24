package com.ink.jdr_server.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ink.jdr_server.dtos.utilisateur.UserUpdateRequest;
import com.ink.jdr_server.entities.Utilisateur;
import com.ink.jdr_server.repositories.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository userRepo;

    @InjectMocks
    private UtilisateurService userService;

    @Test
    void inscrire_ShouldSaveUser_WhenDataIsCorrect() {
        // 1. GIVEN (Préparation)
        Utilisateur user = new Utilisateur();
        user.setPseudo("Grog");
        user.setMail("grog@barbare.fr");

        when(userRepo.existsByPseudo("Grog")).thenReturn(false);
        when(userRepo.existsByMail("grog@barbare.fr")).thenReturn(false);

        // 2. WHEN (Action)
        Utilisateur resultat = userService.inscrire(user);

        // 3. THEN (Vérification)
        assertThat(resultat).isNotNull();
        assertThat(resultat.getIsAdmin()).isFalse();
        verify(userRepo).save(user);
    }

    @Test
    void inscrire_ShouldThrowException_WhenPseudoAlreadyExists() {
        // 1. GIVEN
        Utilisateur user = new Utilisateur();
        user.setPseudo("Grog");

        when(userRepo.existsByPseudo("Grog")).thenReturn(true);

        // 2. WHEN & 3. THEN
        assertThatThrownBy(() -> userService.inscrire(user))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Ce pseudo est déjà utilisé");
        
        verify(userRepo, never()).save(any());
    }

    @Test
    void connexion_ShouldReturnUser_WhenCredentialsAreCorrect() {
        // GIVEN
        Utilisateur user = new Utilisateur();
        user.setMail("test@test.com");
        user.setPassword("secret");
        user.setIsBanned(false);

        when(userRepo.findByMail("test@test.com")).thenReturn(Optional.of(user));

        // WHEN
        Utilisateur result = userService.connexion("test@test.com", "secret");

        // THEN
        assertThat(result.getMail()).isEqualTo("test@test.com");
    }

    @Test
    void connexion_ShouldThrowException_WhenUserIsBanned() {
        // GIVEN
        Utilisateur banni = new Utilisateur();
        banni.setMail("banni@prison.com");
        banni.setIsBanned(true);

        when(userRepo.findByMail("banni@prison.com")).thenReturn(Optional.of(banni));

        // WHEN & THEN
        assertThatThrownBy(() -> userService.connexion("banni@prison.com", "any"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("banni"); 
    }

    @Test
    void modifierProfil_ShouldUpdateOnlyProvidedFields() {
        // GIVEN
        Utilisateur oldUser = new Utilisateur();
        oldUser.setUserId(1L);
        oldUser.setPseudo("AncienPseudo");
        oldUser.setMail("vieux@mail.com");

        UserUpdateRequest request = new UserUpdateRequest("NouveauPseudo", null);

        when(userRepo.findById(1L)).thenReturn(Optional.of(oldUser));

        // WHEN
        Utilisateur result = userService.modifierProfil(1L, request);

        // THEN
        assertThat(result.getPseudo()).isEqualTo("NouveauPseudo");
        assertThat(result.getMail()).isEqualTo("vieux@mail.com");
    }

    @Test
    void supprimerUtilisateur_ShouldThrowException_WhenIdDoesNotExist() {
        // GIVEN
        when(userRepo.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        assertThatThrownBy(() -> userService.supprimerUtilisateur(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Utilisateur non trouvé");
    }

    @Test
    void modifierProfil_Password_ShouldWork() {
    // GIVEN
    Utilisateur user = new Utilisateur();
    user.setUserId(1L);
    user.setPassword("oldPass");
    when(userRepo.findById(1L)).thenReturn(Optional.of(user));

    // WHEN
    Utilisateur result = userService.modifierProfil(1L, "newPass");

    // THEN
    assertThat(result.getPassword()).isEqualTo("newPass");
    }

    @Test
    void connexion_ShouldThrowException_WhenPasswordIsWrong() {
    // GIVEN
    Utilisateur user = new Utilisateur();
    user.setMail("test@test.com");
    user.setPassword("vraiPassword");
    when(userRepo.findByMail("test@test.com")).thenReturn(Optional.of(user));

    // WHEN & THEN
    assertThatThrownBy(() -> userService.connexion("test@test.com", "mauvaisPassword"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Identifiants incorrects");
    }

    @Test
    void bannirUtilisateur_ShouldThrowException_WhenUserNotFound() {
    // GIVEN
    when(userRepo.findById(99L)).thenReturn(Optional.empty());

    // WHEN & THEN
    assertThatThrownBy(() -> userService.bannirUtilisateur(99L))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Utilisateur non trouvé");
    }
    
}
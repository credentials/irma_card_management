package org.irmacard.cardmanagement;

import org.irmacard.credentials.info.CredentialDescription;

public interface CredentialSelector {
	void selectCredential(CredentialDescription credential);
}

package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(private val accountRepository: AccountRepository, private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase) {
    private val allAccounts: Flow<NetworkState> = accountRepository.getAccounts()
    operator fun invoke(query: Flow<String>): Flow<List<Account>?> =
        query
            .debounce(300) // Debounce ở đây
            .distinctUntilChanged()
            .flatMapLatest { query ->
                allAccounts
                    .filterIsInstance<NetworkState.Success<List<Account>>>()
                    .map { state ->
                        val accounts = state.data ?: emptyList()
                        if (query.isBlank()) emptyList()
                        else accounts.filter {
                            it.uid != getCurrentUserIdUseCase() &&
                            it.email.contains(query, ignoreCase = true)
                        }.take(5)
                    }
            }
}
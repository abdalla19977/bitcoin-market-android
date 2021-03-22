package com.mertceyhan.bitcoinmarket.features.market.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertceyhan.bitcoinmarket.base.ui.LayoutViewState
import com.mertceyhan.bitcoinmarket.features.market.domain.model.MarketInformationTimespan
import com.mertceyhan.bitcoinmarket.features.market.domain.usecase.MarketInformationUseCase
import com.mertceyhan.bitcoinmarket.utils.extensions.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketInformationUseCase: MarketInformationUseCase
) : ViewModel() {

    private val marketViewStateLiveData = MutableLiveData<MarketViewState>()

    private val layoutViewStateLiveData = MutableLiveData<LayoutViewState>()

    fun getMarketViewStateLiveData(): LiveData<MarketViewState> = marketViewStateLiveData

    fun getLayoutViewStateLiveData(): LiveData<LayoutViewState> = layoutViewStateLiveData

    fun getMarketInformation(timespan: MarketInformationTimespan) {
        viewModelScope.launch {
            marketInformationUseCase
                .getMarketInformation(timespan)
                .doOnSuccess { marketInformation ->
                    marketViewStateLiveData.value = MarketViewState(marketInformation)
                }
                .collect { state ->
                    layoutViewStateLiveData.value =
                        LayoutViewState(state)
                }
        }
    }
}

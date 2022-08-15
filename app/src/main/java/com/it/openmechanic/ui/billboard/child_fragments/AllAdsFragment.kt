package com.it.openmechanic.ui.billboard.child_fragments

import com.it.openmechanic.databinding.FragmentAllAdsBinding
import com.it.openmechanic.ui.base.BaseMVVMFragment
import com.it.openmechanic.ui.billboard.BillBoardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AllAdsFragment : BaseMVVMFragment<FragmentAllAdsBinding, BillBoardViewModel>() {

    override val viewModel: BillBoardViewModel by sharedViewModel()

    override fun observe() {

    }
}
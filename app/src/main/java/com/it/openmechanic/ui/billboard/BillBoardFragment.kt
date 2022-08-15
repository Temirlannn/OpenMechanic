package com.it.openmechanic.ui.billboard

import com.it.openmechanic.databinding.FragmentBillBoardBinding
import com.it.openmechanic.ui.base.BaseMVVMFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BillBoardFragment : BaseMVVMFragment<FragmentBillBoardBinding, BillBoardViewModel>() {
    override val viewModel: BillBoardViewModel by sharedViewModel()
    override fun observe() {

    }
}
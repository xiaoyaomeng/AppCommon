package com.panghu.uikit.permission

import android.os.Bundle
import android.view.View
import com.panghu.uikit.R
import com.panghu.uikit.base.activity.AbstractBaseActivity
import com.panghu.uikit.view.EmptyView
import com.panghu.uikit.utils.ExternalAppUtil

class PermissionHintActivity : AbstractBaseActivity() {
    private lateinit var emptyView: EmptyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_hint_activity)
        emptyView = findViewById(R.id.empty_view)
        emptyView.setButtonClickListener(View.OnClickListener {
            ExternalAppUtil.go2SystemAppSetting(this@PermissionHintActivity)
            finish()
        })
        emptyView.visibility = View.VISIBLE

        intent?.apply {
            val model = this.getSerializableExtra(EXTRA_MODEL) as HintData?
            model?.let {
                setTitle(it.title)
                emptyView.setImageResource(it.image)
                emptyView.setTitle(it.hintTitle)
                emptyView.setContent(it.description)
            }
        }
    }

    companion object {
        const val EXTRA_MODEL = "MODEL"
    }
}
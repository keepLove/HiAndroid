package com.s.android.hiandroid.ui.patterns

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringListInfos
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.patterns.abstract_factory_pattern.AbstractFactoryPattern
import com.s.android.hiandroid.ui.patterns.adapter_pattern.AdapterPattern
import com.s.android.hiandroid.ui.patterns.bridge_pattern.BridgePattern
import com.s.android.hiandroid.ui.patterns.builder_pattern.BuilderPattern
import com.s.android.hiandroid.ui.patterns.chain_pattern.ChainPattern
import com.s.android.hiandroid.ui.patterns.command_pattern.CommandPattern
import com.s.android.hiandroid.ui.patterns.composite_pattern.CompositePattern
import com.s.android.hiandroid.ui.patterns.decorator_pattern.DecoratorPattern
import com.s.android.hiandroid.ui.patterns.facade_pattern.FacadePattern
import com.s.android.hiandroid.ui.patterns.factory_pattern.FactoryPattern
import com.s.android.hiandroid.ui.patterns.filter_pattern.FilterPattern
import com.s.android.hiandroid.ui.patterns.flyweight_pattern.FlyweightPattern
import com.s.android.hiandroid.ui.patterns.interpreter_pattern.InterpreterPattern
import com.s.android.hiandroid.ui.patterns.iterator_pattern.IteratorPattern
import com.s.android.hiandroid.ui.patterns.mediator_pattern.MediatorPattern
import com.s.android.hiandroid.ui.patterns.memento_pattern.MementoPattern
import com.s.android.hiandroid.ui.patterns.null_object_pattern.NullObjectPattern
import com.s.android.hiandroid.ui.patterns.observer_pattern.ObserverPattern
import com.s.android.hiandroid.ui.patterns.prototype_pattern.PrototypePattern
import com.s.android.hiandroid.ui.patterns.proxy_pattern.ProxyPattern
import com.s.android.hiandroid.ui.patterns.singleton_pattern.SingletonPattern
import com.s.android.hiandroid.ui.patterns.state_pattern.StatePattern
import com.s.android.hiandroid.ui.patterns.strategy_pattern.StrategyPattern
import com.s.android.hiandroid.ui.patterns.template_pattern.TemplatePattern
import com.s.android.hiandroid.ui.patterns.visitor_pattern.VisitorPattern

class PatternsActivity : BaseStringListActivity() {

    private val contentList = mutableListOf<String>()
    private val practiceList = mutableListOf<String>()

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        //
        contentList.add(FactoryPattern.content)
        practiceList.add(FactoryPattern.practice)
        //
        contentList.add(AbstractFactoryPattern.content)
        practiceList.add(AbstractFactoryPattern.practice)
        //
        contentList.add(SingletonPattern.content)
        practiceList.add(SingletonPattern.practice)
        //
        contentList.add(BuilderPattern.content)
        practiceList.add(BuilderPattern.practice)
        //
        contentList.add(AdapterPattern.content)
        practiceList.add(AdapterPattern.practice)
        //
        contentList.add(DecoratorPattern.content)
        practiceList.add(DecoratorPattern.practice)
        //
        contentList.add(FacadePattern.content)
        practiceList.add(FacadePattern.practice)
        //
        contentList.add(ProxyPattern.content)
        practiceList.add(ProxyPattern.practice)
        //
        contentList.add(ChainPattern.content)
        practiceList.add(ChainPattern.practice)
        //
        contentList.add(ObserverPattern.content)
        practiceList.add(ObserverPattern.practice)
        //
        contentList.add(StrategyPattern.content)
        practiceList.add(StrategyPattern.practice)
        //
        contentList.add(CompositePattern.content)
        practiceList.add(CompositePattern.practice)
        //
        contentList.add(PrototypePattern.content)
        practiceList.add(PrototypePattern.practice)
        //
        contentList.add(MementoPattern.content)
        practiceList.add(MementoPattern.practice)
        //
        contentList.add(BridgePattern.content)
        practiceList.add(BridgePattern.practice)
        //
        contentList.add(FilterPattern.content)
        practiceList.add(FilterPattern.practice)
        //
        contentList.add(FlyweightPattern.content)
        practiceList.add(FlyweightPattern.practice)
        //
        contentList.add(CommandPattern.content)
        practiceList.add(CommandPattern.practice)
        //
        contentList.add(InterpreterPattern.content)
        practiceList.add(InterpreterPattern.practice)
        //
        contentList.add(IteratorPattern.content)
        practiceList.add(IteratorPattern.practice)
        //
        contentList.add(MediatorPattern.content)
        practiceList.add(MediatorPattern.practice)
        //
        contentList.add(StatePattern.content)
        practiceList.add(StatePattern.practice)
        //
        contentList.add(NullObjectPattern.content)
        practiceList.add(NullObjectPattern.practice)
        //
        contentList.add(TemplatePattern.content)
        practiceList.add(TemplatePattern.practice)
        //
        contentList.add(VisitorPattern.content)
        practiceList.add(VisitorPattern.practice)
    }

    override fun getItems(): MutableList<StringListInfo> {
        return getStringListInfos(R.array.patterns_list, BasePatternsActivity::class.java)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        startPatternActivity(stringListAdapter.getItem(position)?.title, contentList[position], practiceList[position])
    }

    private fun startPatternActivity(title: String?, content: String?, practice: String?) {
        startActivity(Intent(this, BasePatternsActivity::class.java).apply {
            putExtra("title", title)
            putExtra("content", content)
            putExtra("practice", practice)
        })
    }
}

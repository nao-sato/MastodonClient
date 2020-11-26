package io.keiji.sample.mastodonclient.ui.toot_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.keiji.sample.mastodonclient.R
import io.keiji.sample.mastodonclient.databinding.ListItemTootBinding
import io.keiji.sample.mastodonclient.entity.Toot

class TootListAdapter (//最終的にこいつはViewHolderのリスト、Tootのセルを使い回す機能、送られてきたデータの更新機能を持っている。
    private val layoutInflater: LayoutInflater,
    //↓↓リサイクラービューを作るときは何らかのリストがいる(tootリストはフラグメントで作ってる）
    private val tootList: ArrayList<Toot>,
    private val callback: Callback?
//ここがアダプターを作ろうとした時の決まった記述
): RecyclerView.Adapter<TootListAdapter.ViewHolder>(){//ここのViewHolderに各メソッドの戻り値が送られてくる


    interface Callback{
        fun openDetail(toot: Toot)
        fun delete(toot: Toot)
    }
    //読み込まれた投稿の数を数えてくれる
    override fun getItemCount() = tootList.size

    //ここではリサイクラービューのリストの一つ一つのセルが作られている
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {//戻り値　
        val binding = DataBindingUtil.inflate<ListItemTootBinding>(
            layoutInflater,//一つのTootのレイアウトを一つのビューとして扱うときにこの記述、つまりインフレーターを使う
            R.layout.list_item_toot,//レイアウトのURL
            parent,
            false
        )
        return ViewHolder(binding,callback)
    }

    //ViewHolderの中身を更新するメソッド
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(tootList[position])
        //更新しようとする時のデータが何番目（position）なのかを教えてくれて、その時のデータをバインドする
    }

    class ViewHolder(
        private val binding: ListItemTootBinding,
        private val callback: Callback?
    //binding.rootでビューが取れる（constraintlayoutのとこ）ここのスーパークラスの引数にはviewをいれる必要がある
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(toot: Toot){
           binding.toot = toot
            binding.root.setOnClickListener {
                callback?.openDetail(toot)
            }
            binding.more.setOnClickListener{
                PopupMenu(itemView.context,it).also { popupMenu ->
                    popupMenu.menuInflater.inflate(R.menu.list_item_toot,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {menuItem ->
                        when(menuItem.itemId) {
                        R.id.menu_delete -> callback?.delete(toot)
                        }
                        return@setOnMenuItemClickListener true
                    }
                }.show()
            }
        }
    }
}

//onCreateViewHolderで作られるセルはせいぜい20~30個程度。　それ以上は画面に情報を表示する上で必要ではない。
//しかし送られてくるのデータ（ここで言う投稿）の数はもっとある。
// そこでonBindViewHolderで見えなくなったビュー（セル）の情報は捨てられて、新しく更新されたデータを見えなくなったビューに載せる。
// ビューの再利用→リサイクラービュー*/
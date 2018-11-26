package com.assistant.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.apache.http.client.ClientProtocolException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.assistant.model.ConsumerModel;
import com.assistant.model.TextMessage;
import com.assistant.service.PhoneReceiver;
import com.assistant.speech.setting.IatSettings;
import com.assistant.speech.util.ApkInstaller;
import com.assistant.speech.util.JsonParser;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.example.assistant.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class Register extends Activity implements OnClickListener,OnLongClickListener{
	 private int[] city = { R.array.beijin_province_item,  
	            R.array.tianjin_province_item, R.array.heibei_province_item,  
	            R.array.shanxi1_province_item, R.array.neimenggu_province_item,  
	            R.array.liaoning_province_item, R.array.jilin_province_item,  
	            R.array.heilongjiang_province_item, R.array.shanghai_province_item,  
	            R.array.jiangsu_province_item, R.array.zhejiang_province_item,  
	            R.array.anhui_province_item, R.array.fujian_province_item,  
	            R.array.jiangxi_province_item, R.array.shandong_province_item,  
	            R.array.henan_province_item, R.array.hubei_province_item,  
	            R.array.hunan_province_item, R.array.guangdong_province_item,  
	            R.array.guangxi_province_item, R.array.hainan_province_item,  
	            R.array.chongqing_province_item, R.array.sichuan_province_item,  
	            R.array.guizhou_province_item, R.array.yunnan_province_item,  
	            R.array.xizang_province_item, R.array.shanxi2_province_item,  
	            R.array.gansu_province_item, R.array.qinghai_province_item,  
	            R.array.linxia_province_item, R.array.xinjiang_province_item,  
	            R.array.hongkong_province_item, R.array.aomen_province_item,  
	            R.array.taiwan_province_item };  
	    private int[] countyOfBeiJing = { R.array.beijin_city_item0,  
	            R.array.beijin_city_item1 };  
	    private int[] countyOfTianJing = { R.array.tianjin_city_item0,  
	            R.array.tianjin_city_item1 };  
	    private int[] countyOfHeBei = { R.array.shijiazhuang_city_item,  
	            R.array.tangshan_city_item, R.array.qinghuangdao_city_item,  
	            R.array.handan_city_item, R.array.xingtai_city_item,  
	            R.array.baoding_city_item, R.array.zhangjiakou_city_item,  
	            R.array.chengde_city_item, R.array.cangzhou_city_item,  
	            R.array.langfang_city_item, R.array.hengshui_city_item };  
	    private int[] countyOfShanXi1 = { R.array.taiyuan_city_item,  
	            R.array.datong_city_item, R.array.yangquan_city_item,  
	            R.array.changzhi_city_item, R.array.jincheng_city_item,  
	            R.array.shuozhou_city_item, R.array.jinzhong_city_item,  
	            R.array.yuncheng_city_item, R.array.xinzhou_city_item,  
	            R.array.linfen_city_item, R.array.lvliang_city_item };  
	    private int[] countyOfNeiMengGu = { R.array.huhehaote_city_item,  
	            R.array.baotou_city_item, R.array.wuhai_city_item,  
	            R.array.chifeng_city_item, R.array.tongliao_city_item,  
	            R.array.eerduosi_city_item, R.array.hulunbeier_city_item,  
	            R.array.bayannaoer_city_item, R.array.wulanchabu_city_item,  
	            R.array.xinganmeng_city_item, R.array.xilinguolemeng_city_item,  
	            R.array.alashanmeng_city_item };  
	    private int[] countyOfLiaoNing = { R.array.shenyang_city_item,  
	            R.array.dalian_city_item, R.array.anshan_city_item,  
	            R.array.wushun_city_item, R.array.benxi_city_item,  
	            R.array.dandong_city_item, R.array.liaoning_jinzhou_city_item,  
	            R.array.yingkou_city_item, R.array.fuxin_city_item,  
	            R.array.liaoyang_city_item, R.array.panjin_city_item,  
	            R.array.tieling_city_item, R.array.zhaoyang_city_item,  
	            R.array.huludao_city_item };  
	    private int[] countyOfJiLin = { R.array.changchun_city_item,  
	            R.array.jilin_city_item, R.array.siping_city_item,  
	            R.array.liaoyuan_city_item, R.array.tonghua_city_item,  
	            R.array.baishan_city_item, R.array.songyuan_city_item,  
	            R.array.baicheng_city_item, R.array.yanbian_city_item };  
	    private int[] countyOfHeiLongJiang = { R.array.haerbing_city_item,  
	            R.array.qiqihaer_city_item, R.array.jixi_city_item,  
	            R.array.hegang_city_item, R.array.shuangyashan_city_item,  
	            R.array.daqing_city_item, R.array.heilongjiang_yichun_city_item,  
	            R.array.jiamusi_city_item, R.array.qitaihe_city_item,  
	            R.array.mudanjiang_city_item, R.array.heihe_city_item,  
	            R.array.suihua_city_item, R.array.daxinganling_city_item };  
	    private int[] countyOfShangHai = { R.array.shanghai_city_item0,  
	            R.array.shanghai_city_item1 };  
	  
	    private int[] countyOfJiangSu = { R.array.nanjing_city_item,  
	            R.array.wuxi_city_item, R.array.xuzhou_city_item,  
	            R.array.changzhou_city_item, R.array.nanjing_suzhou_city_item,  
	            R.array.nantong_city_item, R.array.lianyungang_city_item,  
	            R.array.huaian_city_item, R.array.yancheng_city_item,  
	            R.array.yangzhou_city_item, R.array.zhenjiang_city_item,  
	            R.array.jiangsu_taizhou_city_item, R.array.suqian_city_item };  
	    private int[] countyOfZheJiang = { R.array.hangzhou_city_item,  
	            R.array.ningbo_city_item, R.array.wenzhou_city_item,  
	            R.array.jiaxing_city_item, R.array.huzhou_city_item,  
	            R.array.shaoxing_city_item, R.array.jinhua_city_item,  
	            R.array.quzhou_city_item, R.array.zhoushan_city_item,  
	            R.array.zejiang_huzhou_city_item, R.array.lishui_city_item };  
	    private int[] countyOfAnHui = { R.array.hefei_city_item,  
	            R.array.wuhu_city_item, R.array.bengbu_city_item,  
	            R.array.huainan_city_item, R.array.maanshan_city_item,  
	            R.array.huaibei_city_item, R.array.tongling_city_item,  
	            R.array.anqing_city_item, R.array.huangshan_city_item,  
	            R.array.chuzhou_city_item, R.array.fuyang_city_item,  
	            R.array.anhui_suzhou_city_item, R.array.chaohu_city_item,  
	            R.array.luan_city_item, R.array.haozhou_city_item,  
	            R.array.chizhou_city_item, R.array.xuancheng_city_item };  
	    private int[] countyOfFuJian = { R.array.huzhou_city_item,  
	            R.array.xiamen_city_item, R.array.putian_city_item,  
	            R.array.sanming_city_item, R.array.quanzhou_city_item,  
	            R.array.zhangzhou_city_item, R.array.nanp_city_item,  
	            R.array.longyan_city_item, R.array.ningde_city_item };  
	    private int[] countyOfJiangXi = { R.array.nanchang_city_item,  
	            R.array.jingdezhen_city_item, R.array.pingxiang_city_item,  
	            R.array.jiujiang_city_item, R.array.xinyu_city_item,  
	            R.array.yingtan_city_item, R.array.ganzhou_city_item,  
	            R.array.jian_city_item, R.array.jiangxi_yichun_city_item,  
	            R.array.jiangxi_wuzhou_city_item, R.array.shangrao_city_item };  
	    private int[] countyOfShanDong = { R.array.jinan_city_item,  
	            R.array.qingdao_city_item, R.array.zaobo_city_item,  
	            R.array.zaozhuang_city_item, R.array.dongying_city_item,  
	            R.array.yantai_city_item, R.array.weifang_city_item,  
	            R.array.jining_city_item, R.array.taian_city_item,  
	            R.array.weihai_city_item, R.array.rizhao_city_item,  
	            R.array.laiwu_city_item, R.array.linxi_city_item,  
	            R.array.dezhou_city_item, R.array.liaocheng_city_item,  
	            R.array.shandong_bingzhou_city_item, R.array.heze_city_item };  
	    private int[] countyOfHeNan = { R.array.zhenshou_city_item,  
	            R.array.kaifang_city_item, R.array.luoyang_city_item,  
	            R.array.kaipingshan_city_item, R.array.anyang_city_item,  
	            R.array.hebi_city_item, R.array.xinxiang_city_item,  
	            R.array.jiaozuo_city_item, R.array.buyang_city_item,  
	            R.array.xuchang_city_item, R.array.leihe_city_item,  
	            R.array.sanmenxia_city_item, R.array.nanyang_city_item,  
	            R.array.shangqiu_city_item, R.array.xinyang_city_item,  
	            R.array.zhoukou_city_item, R.array.zhumadian_city_item };  
	    private int[] countyOfHuBei = { R.array.wuhan_city_item,  
	            R.array.huangshi_city_item, R.array.shiyan_city_item,  
	            R.array.yichang_city_item, R.array.xiangpan_city_item,  
	            R.array.erzhou_city_item, R.array.jinmen_city_item,  
	            R.array.xiaogan_city_item, R.array.hubei_jinzhou_city_item,  
	            R.array.huanggang_city_item, R.array.xianning_city_item,  
	            R.array.suizhou_city_item, R.array.enshi_city_item,  
	            R.array.shenglongjia_city_item };  
	  
	    private int[] countyOfHuNan = { R.array.changsha_city_item,  
	            R.array.zhuzhou_city_item, R.array.xiangtan_city_item,  
	            R.array.hengyang_city_item, R.array.shaoyang_city_item,  
	            R.array.yueyang_city_item, R.array.changde_city_item,  
	            R.array.zhangjiajie_city_item, R.array.yiyang_city_item,  
	            R.array.hunan_bingzhou_city_item, R.array.yongzhou_city_item,  
	            R.array.huaihua_city_item, R.array.loudi_city_item,  
	            R.array.xiangxi_city_item };  
	    private int[] countyOfGuangDong = { R.array.guangzhou_city_item,  
	            R.array.shaoguan_city_item, R.array.shenzhen_city_item,  
	            R.array.zhuhai_city_item, R.array.shantou_city_item,  
	            R.array.foshan_city_item, R.array.jiangmen_city_item,  
	            R.array.zhangjiang_city_item, R.array.maoming_city_item,  
	            R.array.zhaoqing_city_item, R.array.huizhou_city_item,  
	            R.array.meizhou_city_item, R.array.shanwei_city_item,  
	            R.array.heyuan_city_item, R.array.yangjiang_city_item,  
	            R.array.qingyuan_city_item, R.array.dongguan_city_item,  
	            R.array.zhongshan_city_item, R.array.chaozhou_city_item,  
	            R.array.jiyang_city_item, R.array.yunfu_city_item };  
	    private int[] countyOfGuangXi = { R.array.nanning_city_item,  
	            R.array.liuzhou_city_item, R.array.guilin_city_item,  
	            R.array.guangxi_wuzhou_city_item, R.array.beihai_city_item,  
	            R.array.fangchenggang_city_item, R.array.qinzhou_city_item,  
	            R.array.guigang_city_item, R.array.yuelin_city_item,  
	            R.array.baise_city_item, R.array.hezhou_city_item,  
	            R.array.hechi_city_item, R.array.laibing_city_item,  
	            R.array.chuangzuo_city_item };  
	    private int[] countyOfHaiNan = { R.array.haikou_city_item,  
	            R.array.sanya_city_item };  
	    private int[] countyOfChongQing = { R.array.chongqing_city_item0,  
	            R.array.chongqing_city_item1, R.array.chongqing_city_item2 };  
	    private int[] countyOfSiChuan = { R.array.chengdu_city_item,  
	            R.array.zigong_city_item, R.array.panzhihua_city_item,  
	            R.array.luzhou_city_item, R.array.deyang_city_item,  
	            R.array.mianyang_city_item, R.array.guangyuan_city_item,  
	            R.array.suining_city_item, R.array.neijiang_city_item,  
	            R.array.leshan_city_item, R.array.nanchong_city_item,  
	            R.array.meishan_city_item, R.array.yibing_city_item,  
	            R.array.guangan_city_item, R.array.dazhou_city_item,  
	            R.array.yaan_city_item, R.array.bazhong_city_item,  
	            R.array.ziyang_city_item, R.array.abei_city_item,  
	            R.array.ganmu_city_item, R.array.liangshan_city_item };  
	    private int[] countyOfGuiZhou = { R.array.guiyang_city_item,  
	            R.array.lupanshui_city_item, R.array.zhunyi_city_item,  
	            R.array.anshun_city_item, R.array.tongren_city_item,  
	            R.array.qingxinan_city_item, R.array.biji_city_item,  
	            R.array.qingdongnan_city_item, R.array.qingnan_city_item };  
	    private int[] countyOfYunNan = { R.array.kunming_city_item,  
	            R.array.qujing_city_item, R.array.yuexi_city_item,  
	            R.array.baoshan_city_item, R.array.zhaotong_city_item,  
	            R.array.lijiang_city_item, R.array.simao_city_item,  
	            R.array.lingcang_city_item, R.array.chuxiong_city_item,  
	            R.array.honghe_city_item, R.array.wenshan_city_item,  
	            R.array.xishuangbanna_city_item, R.array.dali_city_item,  
	            R.array.dehuang_city_item, R.array.nujiang_city_item,  
	            R.array.diqing_city_item };  
	    private int[] countyOfXiZang = { R.array.lasa_city_item,  
	            R.array.changdu_city_item, R.array.shannan_city_item,  
	            R.array.rgeze_city_item, R.array.naqu_city_item,  
	            R.array.ali_city_item, R.array.linzhi_city_item };  
	  
	    private int[] countyOfShanXi2 = { R.array.xian_city_item,  
	            R.array.tongchuan_city_item, R.array.baoji_city_item,  
	            R.array.xianyang_city_item, R.array.weinan_city_item,  
	            R.array.yanan_city_item, R.array.hanzhong_city_item,  
	            R.array.yulin_city_item, R.array.ankang_city_item,  
	            R.array.shangluo_city_item };  
	    private int[] countyOfGanSu = { R.array.lanzhou_city_item,  
	            R.array.jiayuguan_city_item, R.array.jinchang_city_item,  
	            R.array.baiyin_city_item, R.array.tianshui_city_item,  
	            R.array.wuwei_city_item, R.array.zhangyue_city_item,  
	            R.array.pingliang_city_item, R.array.jiuquan_city_item,  
	            R.array.qingyang_city_item, R.array.dingxi_city_item,  
	            R.array.longnan_city_item, R.array.linxia_city_item,  
	            R.array.gannan_city_item };  
	    private int[] countyOfQingHai = { R.array.xining_city_item,  
	            R.array.haidong_city_item, R.array.haibai_city_item,  
	            R.array.huangnan_city_item, R.array.hainan_city_item,  
	            R.array.guluo_city_item, R.array.yushu_city_item,  
	            R.array.haixi_city_item };  
	    private int[] countyOfNingXia = { R.array.yinchuan_city_item,  
	            R.array.shizuishan_city_item, R.array.wuzhong_city_item,  
	            R.array.guyuan_city_item, R.array.zhongwei_city_item };  
	    private int[] countyOfXinJiang = { R.array.wulumuqi_city_item,  
	            R.array.kelamayi_city_item, R.array.tulyfan_city_item,  
	            R.array.hami_city_item, R.array.changji_city_item,  
	            R.array.boertala_city_item, R.array.bayinguolen_city_item,  
	            R.array.akesu_city_item, R.array.kemuleisu_city_item,  
	            R.array.geshen_city_item, R.array.hetian_city_item,  
	            R.array.yili_city_item, R.array.tacheng_city_item,  
	            R.array.aleitai_city_item, R.array.shihezi_city_item,  
	            R.array.alaer_city_item, R.array.tumushihe_city_item,  
	            R.array.wujiaqu_city_item };  
	    private int[] countyOfHongKong = {};  
	    private int[] countyOfAoMen = {};  
	    private int[] countyOfTaiWan = {};  
	private EditText chengjiaojine,name,phone,edtYisuan,edtYixiang,edtYixiangyetai,edtYonghuzu,edtKehushuxing,edtZijinshili
	,edtRenzhiqudao,edtYixiangmianji,edtYixianghuxing,shengfen,chengshi,quxian,quyu,louhao,shenfenzheng;
	
	private ConsumerModel conModel;
	
	private RadioButton imgFeman,imgMan;
	private Button clear,save,back;
	private static String TAG = "IatDemo";
	private	String laoyezhuphone = ""; 
	
	private boolean isRegister = false;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog iatDialog;
	private SharedPreferences mSharedPreferences;
	
	SpeechSynthesizer mTts ;

	// 默认发音人
	private String voicer = "xiaoyan";


	private int position = -1,cityId = -1;
	
	Bitmap bmp;
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;

	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_LOCAL;

	private Toast mToast;
	ImageButton imgVoice,imgMore;
	EditText beizhu;
	
	int ret = 0;// 函数调用返回值
	
	String type;
//	private ProgressDialog pDia = null;
	LinearLayout show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.register);
		type = getIntent().getExtras().getString("type");
		
		if(type.equals("editer")){
			conModel = (ConsumerModel) getIntent().getExtras().get("consumer");
		}
		SpeechUtility.createUtility(this, "appid=54547b14");
		initUI();
		initListener();
		initDtae();
	}

	private void initDtae() {
		if(conModel == null){
			phone.setText(getIntent().getExtras().getString("phone"));
			if(Constant.QUYU.equals("")){
				 SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
				 Constant.QUYU = sp.getString("USER_QUYU", "");
			}
			quyu.setText(Constant.QUYU);
		}else{
			phone.setText(conModel.getCustomer_phone());
			name.setText(conModel.getCustomer_name());
			
			conModel.setConsultantname(Constant.USERNAME);
			conModel.setConsultantphone(Constant.USERPHONE);
			beizhu.setText(conModel.getBeizhu());
			
			
			if(!conModel.getDizhi().equals("")){
				String[] arr = conModel.getDizhi().split("-");
				shengfen.setText(arr[0]);
				chengshi.setText(arr[1]);
				quxian.setText(arr[2]);
				edtKehushuxing.setText(conModel.getKehushuxing());
				edtYonghuzu.setText(conModel.getKehuzu());
				edtRenzhiqudao.setText(conModel.getRenzhiqudao());
			}
			
			
			if(conModel.getXingbie().equals("男")){
				imgMan.setChecked(true);
				imgFeman.setChecked(false);
			}else{
				imgMan.setChecked(false);
				imgFeman.setChecked(true);
			}
			
			if(conModel.getKehushuxing().equals("新客户")){
				chengjiaojine.setEnabled(false);
       			chengjiaojine.setHint("老业主可写");
			}else{
       			chengjiaojine.setHint("必填");
       			shenfenzheng.setHint("必填");
			}
			edtZijinshili.setText(conModel.getZijinshili());
			edtYixiangyetai.setText(conModel.getYixiangyetai());
			edtYixiangmianji.setText(conModel.getYixiangmianji());
			edtYixianghuxing.setText(conModel.getYixianghuxing());
			edtYixiang.setText(conModel.getYixiang());
			edtYisuan.setText(conModel.getYisuan());
			if(!conModel.getShenfenzheng().equals(""))
				shenfenzheng.setText(conModel.getShenfenzheng());
			if(Constant.QUYU.equals("")){
				 SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
				 Constant.QUYU = sp.getString("USER_QUYU", "");
			}
			quyu.setText(conModel.getQuyu());
			louhao.setText(conModel.getLouhao());
			chengjiaojine.setText(conModel.getChengjiaojine());
		}
	}

//	private void showDialog(){
//		pDia = new ProgressDialog(Register.this);
//		pDia.setMessage("正在努力更新 请稍后。。。。");
//		pDia.setCancelable(false);
//		pDia.setCanceledOnTouchOutside(false);
//		pDia.show();
//	}
	
	private void initListener() {
		imgVoice.setOnClickListener(this);
		imgVoice.setOnLongClickListener(this);
		imgMore.setOnClickListener(this);
//		imgPhoto.setOnClickListener(this);
		imgMan.setOnClickListener(this);
		imgFeman.setOnClickListener(this);
		clear.setOnClickListener(this);
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		
		edtYisuan.setFocusable(false);
		edtYixiang.setFocusable(false);
		edtYixiangyetai.setFocusable(false);
		edtYonghuzu.setFocusable(false);
		edtZijinshili.setFocusable(false);
		edtKehushuxing.setFocusable(false);
		edtYixiangmianji.setFocusable(false);
		edtRenzhiqudao.setFocusable(false);
		edtYixianghuxing.setFocusable(false);
		shengfen.setFocusable(false);
		chengshi.setFocusable(false);
		quxian.setFocusable(false);
		quyu.setFocusable(false);
		
		edtYisuan.setLongClickable(false);
		edtYixiang.setLongClickable(false);
		edtYixiangyetai.setLongClickable(false);
		edtYonghuzu.setLongClickable(false);
		edtZijinshili.setLongClickable(false);
		edtKehushuxing.setLongClickable(false);
		edtRenzhiqudao.setLongClickable(false);
		edtYixiangmianji.setLongClickable(false);
		shengfen.setLongClickable(false);
		chengshi.setLongClickable(false);
		quxian.setLongClickable(false);
		edtYixianghuxing.setLongClickable(false);
		quyu.setLongClickable(false);
		
		
		edtYisuan.setOnClickListener(this);
		edtYixiang.setOnClickListener(this);
		edtYixiangyetai.setOnClickListener(this);
		edtYonghuzu.setOnClickListener(this);
		edtZijinshili.setOnClickListener(this);
		edtKehushuxing.setOnClickListener(this);
		edtRenzhiqudao.setOnClickListener(this);
		edtYixiangmianji.setOnClickListener(this);
		edtYixianghuxing.setOnClickListener(this);
		shengfen.setOnClickListener(this);
		chengshi.setOnClickListener(this);
		quxian.setOnClickListener(this);
		quyu.setOnClickListener(this);
		
		
		edtKehushuxing.addTextChangedListener(new EdtTextWatch());
	}
	class EdtTextWatch implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			if(edtKehushuxing.getText().toString().equals("新客户")){
				chengjiaojine.setEnabled(false);
       			chengjiaojine.setHint("新客户不填");
       			shenfenzheng.setHint("");
       			louhao.setHint("");
       		    laoyezhuphone = "";
       		    
       		    if(conModel != null){
       		    	conModel.setKehushuxing("新客户");
       		    }
       		    
			}else if(edtKehushuxing.getText().toString().equals("老带新")){
       			chengjiaojine.setEnabled(true);
       			chengjiaojine.setHint("必填");
       			shenfenzheng.setHint("必填");
       			louhao.setHint("必填");
       		  if(conModel != null){
     		    	conModel.setKehushuxing("老带新");
     		    }
			}else{
				laoyezhuphone = "";
       			chengjiaojine.setEnabled(true);
       			chengjiaojine.setHint("必填");
       			shenfenzheng.setHint("必填");
       			louhao.setHint("必填");
       		  if(conModel != null){
   		    	conModel.setKehushuxing("老业主");
   		    }
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}
	}
	private void initUI() {
		show = (LinearLayout) findViewById(R.id.show);
		imgVoice = (ImageButton) findViewById(R.id.imgVoice);
		beizhu = (EditText) findViewById(R.id.beizhu);
		imgMore = (ImageButton) findViewById(R.id.imgMore);
		edtYisuan = (EditText) findViewById(R.id.yisuan);
		edtYixiang = (EditText) findViewById(R.id.yixiang);
		edtYixiangyetai = (EditText) findViewById(R.id.yixiangyetai);
		edtYonghuzu = (EditText) findViewById(R.id.yonghuzu);
		edtKehushuxing = (EditText) findViewById(R.id.kehushuxing);
		edtZijinshili = (EditText) findViewById(R.id.zijinshili);
		edtYixiangmianji = (EditText) findViewById(R.id.yixiangmianji);
		edtRenzhiqudao = (EditText) findViewById(R.id.renzhiqudao);
		edtYixianghuxing = (EditText) findViewById(R.id.yixianghuxing);
		imgMan = (RadioButton) findViewById(R.id.man);
		imgFeman = (RadioButton) findViewById(R.id.feman);
		shengfen = (EditText) findViewById(R.id.shengfen);
		chengshi = (EditText) findViewById(R.id.chengshi);
		quxian = (EditText) findViewById(R.id.quxian);
		clear = (Button) findViewById(R.id.clear);
		save = (Button) findViewById(R.id.save);
		back = (Button) findViewById(R.id.back);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);
		quyu = (EditText) findViewById(R.id.quyu);
		louhao = (EditText) findViewById(R.id.louhao);
		shenfenzheng = (EditText) findViewById(R.id.shenfenzheng);
		chengjiaojine = (EditText) findViewById(R.id.chengjiaojine);
		
		if(type.equals("fastsave") || type.equals("fastsaveAndInsertCall")){
			show.setVisibility(View.GONE);
		}
		
		
		if(!edtKehushuxing.getText().toString().equals("新客户")){
			chengjiaojine.setEnabled(true);
		}
		
		imgMan.setChecked(true);
		
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(this, mInitListener);
		mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,Activity.MODE_PRIVATE);
		setTsParam(mTts);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		case R.id.imgVoice:
			setParam();
			boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
			if (isShowDialog) {
				// 显示听写对话框
				iatDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						if(mIat.isListening()){
							mIat.stopListening();
							mIat.cancel();
							
						}
					}
				});
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
				showTip(getString(R.string.text_begin));
			} else {
				// 不显示听写对话框
				ret = mIat.startListening(recognizerListener);
				if(ret != ErrorCode.SUCCESS){
					showTip("听写失败,错误码：" + ret);
				}else {
					showTip(getString(R.string.text_begin));
				}
			}
			break;
		}
		return false;
	}
	
	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}		
		}
	};
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}
		}
	};
	
	private void showTip(final String str)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}
	
	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	@SuppressLint("SdCardPath")
	public void setParam(){
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "/sdcard/iflytek/wavaudio.pcm");
	}
	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			mPercentForBuffering = percent;
			mToast.setText(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
			
			mToast.show();
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			mPercentForPlaying = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if(error == null)
			{
				showTip("播放完成");
			}
			else if(error != null)
			{
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			
		}
	};
	  public boolean isNumeric(String str) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	private void setDialog(final int arrId,final EditText edt,final EditText edt1,final EditText edt2){
		 new AlertDialog.Builder(Register.this) // build AlertDialog  
        .setTitle("选择")
        .setItems(arrId, new DialogInterface.OnClickListener() { //content  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
           	 edt.setText(getResources().getStringArray(arrId)[which]);
           	 
           	 if(edt.equals(edtKehushuxing)){
           		chengjiaojine.setHint("");
           		 if(which == 2){
           			laoyezhuphone = "";
           			chengjiaojine.setEnabled(true);
           			chengjiaojine.setHint("必填");
           			shenfenzheng.setHint("必填");
           			louhao.setHint("必填");
           		 }else if(which == 0){
           			 if(conModel != null)
           				 edtKehushuxing.setText(conModel.getKehushuxing());
           			 else
           				edtKehushuxing.setText("新客户");
           			final EditText laoyezhu = new EditText(Register.this);
           			 new AlertDialog.Builder(Register.this) // build AlertDialog  
           	        .setTitle("老业主号码")
           	        .setView(laoyezhu)
           	        .setPositiveButton("确定", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							final String content = laoyezhu.getText().toString().trim();
							if(content.length() != 11 || !isNumeric(content)){
								
								
								 if(conModel != null && !conModel.getKehuleixing().equals("新客户"))
			           				 edtKehushuxing.setText(conModel.getKehushuxing());
			           			 else{
										edtKehushuxing.setText("新客户");
										chengjiaojine.setEnabled(false);
					           			chengjiaojine.setHint("新客户不填");
					           			shenfenzheng.setHint("");
					           			louhao.setHint("");
					           		    laoyezhuphone = "";
			           			 }
								Toast.makeText(Register.this, "请输入11位手机号", 0).show();
							}else{
								PhoneReceiver.showLoad(Register.this,save);
								new Thread(new Runnable() {
									public void run() {
										try {
											TextMessage tMsg = NetworkData.posturl(Constant.ISLAOYEZHU+"&phone="+content);
											if(tMsg != null && tMsg.getContent().equals("是")){
												if(!content.equals(phone.getText().toString())){
													myHandler.sendEmptyMessage(12); 
													laoyezhuphone = content;
												}else{
													myHandler.sendEmptyMessage(13); 
													laoyezhuphone = "";
												}
												
											}else if(tMsg != null && tMsg.getContent().equals("否")){
												myHandler.sendEmptyMessage(11);
							           		    laoyezhuphone = "";
											}
										} catch (ClientProtocolException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}).start();
								
							}
						}
           	        	
           	        })
           	        .show();
           		 }else{
           			chengjiaojine.setEnabled(false);
           			chengjiaojine.setHint("新客户不填");
           			shenfenzheng.setHint("");
           			louhao.setHint("");
           			laoyezhuphone = "";
           		 }
           	 }
           	 
           	 
           	 if(edt.equals(shengfen)){
           		 if(position != which){
           			edt1.setText("城市");
           			edt2.setText("区县");
           		 }
           		 position = which;
           	 }else if(edt.equals(chengshi)){
           		 if(position != which){
            			edt2.setText("区县");
            		 }
           		 cityId = which;
           	 }
            }  
        })  
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss(); //关闭alertDialog  
            }  
        }).show();
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.shengfen:
			setDialog(R.array.province_item,shengfen,chengshi,quxian);
			break;
		case R.id.chengshi:
			if (shengfen.getText().toString().trim().equals("省份")) {  
                AlertDialog dialog = new AlertDialog.Builder(Register.this)  
                        .setMessage("请先选择省份")  
                        .setPositiveButton("确定",  new DialogInterface.OnClickListener() {  
                                    @Override  
                                    public void onClick(  DialogInterface dialog,   int which) {  
                                    }  
                                }).create();  
                dialog.show();  
            }else{
            	
            	if(position == -1){
            		String prin = shengfen.getText().toString();
            		String[] arr = getResources().getStringArray(R.array.province_item);
            		for (int i = 0; i < arr.length; i++) {
						if(prin.equals(arr[i])){
							position = i;
							break;
						}
					}
            	}
            	
            	setDialog(city[position],chengshi,null,quxian);
            }
			
			break;
		case R.id.quxian:
			
			if ((!shengfen.getText().toString().trim().equals("省份"))&&chengshi.getText().toString().trim().equals("城市")) {  
                AlertDialog dialog = new AlertDialog.Builder(  
                        Register.this)  
                        .setMessage("请先选择城市")  
                        .setPositiveButton("确定",  
                                new DialogInterface.OnClickListener() {  

                                    @Override  
                                    public void onClick(DialogInterface dialog,int which) {  
                                    }  
                                }).create();  
                dialog.show();  
            } else if(shengfen.getText().toString().trim().equals("省份")&&chengshi.getText().toString().trim().equals("城市")){  
                AlertDialog dialog = new AlertDialog.Builder(  
                        Register.this)  
                        .setMessage("请先选择省份")  
                        .setPositiveButton("确定",  
                                new DialogInterface.OnClickListener() {  

                                    @Override  
                                    public void onClick(DialogInterface dialog, int which) {  
                                    }  
                                }).create();  
                dialog.show();  
            }else{
            	 switch (position) {  
                 case 0:  
                	 setDialog(countyOfBeiJing[cityId],quxian,null,null);
                     break;  
                 case 1:  
                	 setDialog(countyOfTianJing[cityId],quxian,null,null); 
                     break;  
                 case 2:  
                	 setDialog(countyOfHeBei[cityId],quxian,null,null);
                     break;  
                 case 3:  
                	 setDialog(countyOfShanXi1[cityId],quxian,null,null);
                     break;  
                 case 4:  
                	 setDialog(countyOfNeiMengGu[cityId],quxian,null,null);
                     break;  
                 case 5:  
                	 setDialog(countyOfLiaoNing[cityId],quxian,null,null);
                     break;  
                 case 6:  
                	 setDialog(countyOfJiLin[cityId],quxian,null,null);
                     break;  
                 case 7:  
                	 setDialog(countyOfHeiLongJiang[cityId],quxian,null,null);
                     break;  
                 case 8:  
                	 setDialog(countyOfShangHai[cityId],quxian,null,null);  
                     break;  
                 case 9:  
                	 setDialog(countyOfJiangSu[cityId],quxian,null,null);
                     break;  
                 case 10:  
                	 setDialog(countyOfZheJiang[cityId],quxian,null,null);
                     break;  
                 case 11:  
                	 setDialog(countyOfAnHui[cityId],quxian,null,null); 
                     break;  
                 case 12:  
                	 setDialog(countyOfFuJian[cityId],quxian,null,null); 
                     break;  
                 case 13:  
                	 setDialog(countyOfJiangXi[cityId],quxian,null,null);
                     break;  
                 case 14:  
                	 setDialog(countyOfShanDong[cityId],quxian,null,null); 
                     break;  
                 case 15:  
                	 setDialog(countyOfHeNan[cityId],quxian,null,null);
                     break;  
                 case 16:  
                	 setDialog(countyOfHuBei[cityId],quxian,null,null);
                     break;  
                 case 17:  
                	 setDialog(countyOfHuNan[cityId],quxian,null,null);
                     break;  
                 case 18:  
                	 setDialog(countyOfGuangDong[cityId],quxian,null,null);
                     break;  
                 case 19:  
                	 setDialog(countyOfGuangXi[cityId],quxian,null,null);
                     break;  
                 case 20:  
                	 setDialog(countyOfHaiNan[cityId],quxian,null,null);
                     break;  
                 case 21:  
                	 setDialog(countyOfChongQing[cityId],quxian,null,null);
                     break;  
                 case 22:  
                	 setDialog(countyOfSiChuan[cityId],quxian,null,null);
                     break;  
                 case 23:  
                	 setDialog(countyOfGuiZhou[cityId],quxian,null,null);
                     break;  
                 case 24:  
                	 setDialog(countyOfYunNan[cityId],quxian,null,null);
                     break;  
                 case 25:  
                	 setDialog(countyOfXiZang[cityId],quxian,null,null);
                     break;  
                 case 26:  
                	 setDialog(countyOfShanXi2[cityId],quxian,null,null);
                     break;  
                 case 27:  
                	 setDialog(countyOfGanSu[cityId],quxian,null,null);
                     break;  
                 case 28:  
                	 setDialog(countyOfQingHai[cityId],quxian,null,null);
                     break;  
                 case 29:  
                	 setDialog(countyOfNingXia[cityId],quxian,null,null);
                     break;  
                 case 30:  
                	 setDialog(countyOfXinJiang[cityId],quxian,null,null);
                     break;  
                 case 31:  
                	 setDialog(countyOfHongKong[cityId],quxian,null,null);
                     break;  
                 case 32:  
                	 setDialog(countyOfAoMen[cityId],quxian,null,null);
                     break;  
                 case 33:  
                	 setDialog(countyOfTaiWan[cityId],quxian,null,null);
                     break;  
        		 }
            	
            }
			break;
		case R.id.yixianghuxing:
			setDialog(R.array.yixianghuxing,edtYixianghuxing,null,null);
			break;
		case R.id.kehushuxing:
			setDialog(R.array.kehushuxing,edtKehushuxing,null,null);
			break;
		case R.id.zijinshili:
			setDialog(R.array.zijinshili,edtZijinshili,null,null);
			break;
		case R.id.yixiang:
			setDialog(R.array.yixiang,edtYixiang,null,null);
			 break;
		case R.id.yisuan:
			setDialog(R.array.yusuan,edtYisuan,null,null);
			break;
		case R.id.yixiangyetai:
			setDialog(R.array.yixiangyetai,edtYixiangyetai,null,null);
			break;
		case R.id.yonghuzu:
			setDialog(R.array.kehuzu,edtYonghuzu,null,null);
			break;
		case R.id.renzhiqudao:
			setDialog(R.array.renzhiqudao,edtRenzhiqudao,null,null);
			break;
		case R.id.yixiangmianji:
			setDialog(R.array.yixiangmianji,edtYixiangmianji,null,null);
			break;
		case R.id.imgPhoto:
			 Intent intent = new Intent();  
             /* 开启Pictures画面Type设定为image */  
             intent.setType("image/*");  
             intent.setAction(Intent.ACTION_GET_CONTENT);   
             startActivityForResult(intent, 1); 
			break;
		case R.id.imgVoice:
			if (!mTts.isSpeaking()) {
				ApkInstaller mInstaller = new ApkInstaller(Register.this);
				// 初始化合成对象
				// 语音合成对象
				String text = beizhu.getText().toString();
				if (!SpeechUtility.getUtility().checkServiceInstalled()) {
					mInstaller.install();
				} else {
					int code = mTts.startSpeaking(text, mTtsListener);
					if (code != ErrorCode.SUCCESS) {
						showTip("语音合成失败,错误码: " + code);
					}
				}
			}else{
				mTts.stopSpeaking();
			}
			break;
		case R.id.imgMore:
			showPresonSelectDialog();
			break;
		case R.id.quyu:
			setDialog(R.array.quyu,quyu,null,null);
			break;
		case R.id.clear:
			
			final EditText edt = new EditText(this);
			new AlertDialog.Builder(Register.this)
			.setTitle("安全密码")
			.setView(edt)
			.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(edt.getText().toString().equals(Constant.SAFEPWD)){
						PhoneReceiver.showLoad(Register.this,save);
	            		new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
								TextMessage t = new NetworkData().posturl(Constant.UNREGISTER+"&phone="+phone.getText().toString());
								if(t != null && t.getContent().equals("更新成功")){
									isRegister = false;
									myHandler.sendEmptyMessage(3);
									finish();
								}else if(t != null && t.getContent().equals("未注册")){
									myHandler.sendEmptyMessage(4);
								}else{
									myHandler.sendEmptyMessage(1);
								}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
	            		dialog.dismiss(); //关闭alertDialog  
					}else
						myHandler.sendEmptyMessage(5);
					dialog.cancel();
					
				}
			}).show();
			
			break;
		case R.id.back:
			finish();
			break;
		case R.id.save:
			if(type.equals("fastsave") || type.equals("fastsaveAndInsertCall")){
				if(conModel == null && getFastConsumerModel() && !isRegister){
					PhoneReceiver.showLoad(Register.this,save);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								TextMessage tMsg = NetworkData.posturl(Constant.REGISTER+"&phone="+phone.getText().toString()+"&userid="
							+Constant.USERPHONE+"&myphone="+Constant.USERPHONE+"&myname="+Constant.USERNAME+"&laoyezhuphone="+laoyezhuphone+"&islaoyezhu="+!conModel.getKehushuxing().equals("新客户")
							+"&username="+conModel.getCustomer_name());
								if(tMsg != null && Integer.parseInt(tMsg.getContent()) > 0){
									TextMessage t = NetworkData.postFastConsumerDataUrl(Constant.FASTUPDATACONSUMERMODEL+"&type=insert", conModel);
									if(t != null && t.getContent().equals("更新成功")){
										if(type.equals("fastsaveAndInsertCall")){
											
											SimpleDateFormat dateForamt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											TextMessage msg = NetworkData.posturl(Constant.INSERTCALL+"&start="+URLEncoder.encode(dateForamt.format(getIntent().getExtras().getLong("stopTime") - getIntent().getExtras().getLong("callTime")*1000),
													"UTF-8")+"&stop="+URLEncoder.encode(dateForamt.format(getIntent().getExtras().getLong("stopTime")), "UTF-8")+"&isCallIn="+getIntent().getStringExtra("flag")
													+"&phone="+phone.getText().toString()+"&myphone="+Constant.USERPHONE);
											if(msg!= null && msg.getContent().equals("更新成功")){
												sendBroadcast(new Intent("updatecall"));
											}
											Constant.isNeedRefresh = true;
										}
										myHandler.sendEmptyMessage(3);
										isRegister = true;
										finish();
									}else{
										myHandler.sendEmptyMessage(1);
									}
								}else{
									myHandler.sendEmptyMessage(2);
								}
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}else{
					if(getFastConsumerModel()){
						PhoneReceiver.showLoad(Register.this,save);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
					TextMessage t;
					try {
						t = NetworkData.postFastConsumerDataUrl(Constant.FASTUPDATACONSUMERMODEL+"&type=update", conModel);
						if(t != null && t.getContent().equals("更新成功")){
							myHandler.sendEmptyMessage(3);
							finish();
						}else{
							myHandler.sendEmptyMessage(1);
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
						}}).start();
					}
				}
			}else{
			if(conModel == null && getConsumerModel() && !isRegister){
				PhoneReceiver.showLoad(Register.this,save);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							TextMessage tMsg = NetworkData.posturl(Constant.REGISTER+"&phone="+phone.getText().toString()+"&userid="
						+Constant.USERPHONE+"&myphone="+Constant.USERPHONE+"&myname="+Constant.USERNAME+"&laoyezhuphone="+laoyezhuphone+"&islaoyezhu="+!conModel.getKehushuxing().equals("新客户")
						+"&username="+conModel.getCustomer_name());
							if(tMsg != null && Integer.parseInt(tMsg.getContent()) > 0){
								TextMessage t = NetworkData.postConsumerDataUrl(Constant.UPDATACONSUMERMODEL+"&type=insert",conModel);
								if(t != null &&t.getContent().equals("更新成功")){
									if(type.equals("saveAndInsertCall")){
										SimpleDateFormat dateForamt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										TextMessage msg = NetworkData.posturl(Constant.INSERTCALL+"&start="+URLEncoder.encode(dateForamt.format(getIntent().getExtras().getLong("stopTime") - getIntent().getExtras().getLong("callTime")*1000),
												"UTF-8")+"&stop="+URLEncoder.encode(dateForamt.format(getIntent().getExtras().getLong("stopTime")), "UTF-8")+"&isCallIn="+getIntent().getStringExtra("flag")
												+"&phone="+phone.getText().toString()+"&myphone="+Constant.USERPHONE);
										if(msg!= null && msg.getContent().equals("更新成功")){
											sendBroadcast(new Intent("updatecall"));
										}
										Constant.isNeedRefresh = true;
									}
									myHandler.sendEmptyMessage(3);
									isRegister = true;
									finish();
								}else{
									if(t != null)
										System.out.println("error = "+t.getContent());
									myHandler.sendEmptyMessage(1);
								}
							}else{
								myHandler.sendEmptyMessage(2);
							}
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}else{
				if(getConsumerModel()){
					PhoneReceiver.showLoad(Register.this,save);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
				TextMessage t;
				try {
					t = NetworkData.postConsumerDataUrl(Constant.UPDATACONSUMERMODEL+"&type=update", conModel);
					if(t != null && t.getContent().equals("更新成功")){
						myHandler.sendEmptyMessage(3);
						finish();
					}else{
						myHandler.sendEmptyMessage(1);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
					}}).start();
				}
			}
			}
			break;
		}
	}
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PhoneReceiver.dimissLoad();
			if(msg.what == 1){
				Toast.makeText(Register.this, "更新失败 请检查网络", 0).show();
			}else if(msg.what == 2){
				Toast.makeText(Register.this, "已经被人注册", 0).show();
			}else if(msg.what == 4){
				Toast.makeText(Register.this, "未注册", 0).show();
			}else if(msg.what == 5){
				Toast.makeText(Register.this, "安全密码错误", 0).show();
			}else if(msg.what == 11){
				Toast.makeText(Register.this, "该号码不是老业主", 0).show();
				laoyezhuphone = "";
				chengjiaojine.setEnabled(false);
    			chengjiaojine.setHint("新客户不填");
    			shenfenzheng.setHint("");
			}else if(msg.what == 12){
				Toast.makeText(Register.this, "是老业主 可以绑定", 0).show();
				
				edtKehushuxing.setText("老带新");
				chengjiaojine.setEnabled(true);
       			chengjiaojine.setHint("必填");
       			shenfenzheng.setHint("必填");
       			louhao.setHint("必填");
			}else if(msg.what == 13){
				Toast.makeText(Register.this, "不能绑定自己!!", 0).show();
			}else{
				Toast.makeText(Register.this, "更新成功!!", 0).show();
				Home.getDate(Register.this,save);
			}
		}
		
	};
	
	private boolean getFastConsumerModel(){
		if(Constant.QUYU.equals("")){
			 SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
			 Constant.QUYU = sp.getString("USER_QUYU", "");
		}
		if(name.getText().toString().equals("") || phone.getText().toString().equals("")){
			Toast.makeText(Register.this, "您有必填项没填", 0).show();
			return false;
		}
		
		if(conModel == null)
			conModel = new ConsumerModel();
		conModel.setConsultantname(Constant.USERNAME);
		conModel.setConsultantphone(Constant.USERPHONE);
		conModel.setCustomer_name(name.getText().toString());
		conModel.setCustomer_phone(phone.getText().toString());
		if(quyu.getText().toString().trim().equals("")){
			conModel.setQuyu(Constant.QUYU);
		}else
			conModel.setQuyu(quyu.getText().toString().trim());
		if(imgMan.isChecked()){
			conModel.setXingbie("男");
		}else
			conModel.setXingbie("女");
		if(!conModel.getBeizhu().equals("")){
			if(conModel.getBeizhu().equals(beizhu.getText().toString())){
				conModel.setBeizhu("");
			}else{
				conModel.setBeizhu(""+beizhu.getText().toString());
			}
		}else{
			conModel.setBeizhu(""+beizhu.getText().toString());
		}
			conModel.setDatatime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			conModel.setTemp9("2000-01-01 01:01:01");
			
		
		return true;
	}
	
	private boolean getConsumerModel(){
		if(Constant.QUYU.equals("")){
			 SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
			 Constant.QUYU = sp.getString("USER_QUYU", "");
		}
		if(name.getText().toString().equals("") || phone.getText().toString().equals("")
				|| edtYixiang.getText().toString().equals("") || edtYonghuzu.getText().toString().equals("") || edtYixianghuxing.getText().toString().equals("")
				|| edtKehushuxing.getText().toString().equals("")){
			Toast.makeText(Register.this, "您有必填项没填", 0).show();
			return false;
		}
		
		if(!chengjiaojine.getText().toString().equals("") || !edtKehushuxing.getText().toString().equals("新客户")){
			if(chengjiaojine.getText().toString().equals("") || !isNumeric(chengjiaojine.getText().toString())){
				Toast.makeText(Register.this, "成交金额必须填写且为数字", 0).show();
				return false;
			}
			else if(edtKehushuxing.getText().toString().equals("") ){
				Toast.makeText(Register.this, "客户属性必须填写", 0).show();
				return false;
			}else if(!isNumeric(chengjiaojine.getText().toString())){
				Toast.makeText(Register.this, "金额填写不正确", 0).show();
				return false;
			}else if(shenfenzheng.getText().toString().equals("") || !isNumeric(shenfenzheng.getText().toString())){
				Toast.makeText(Register.this, "身份证填写不正确", 0).show();
				return false;
			}
		}
		
		
		if(conModel == null){
			conModel = new ConsumerModel();
		}

		if(!conModel.getKehushuxing().equals(edtKehushuxing.getText().toString())){
			if(edtKehushuxing.getText().toString().equals("新客户")){
				conModel.setTemp9("2000-01-01 01:01:01");
			}else{
				conModel.setTemp9(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			}
		}
		
		if(conModel.getTemp9().equals(""))
			conModel.setTemp9("2000-01-01 01:01:01");
		
		if(Constant.QUYU.equals("")){
			 SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
			 Constant.QUYU = sp.getString("USER_QUYU", "");
		}
		
		conModel.setConsultantname(Constant.USERNAME);
		conModel.setConsultantphone(Constant.USERPHONE);
		conModel.setCustomer_name(name.getText().toString());
		conModel.setCustomer_phone(phone.getText().toString());
		
		if(!conModel.getBeizhu().equals("")){
			if(conModel.getBeizhu().equals(beizhu.getText().toString())){
				conModel.setBeizhu("");
			}else{
				conModel.setBeizhu(beizhu.getText().toString());
			}
		}else{
			conModel.setBeizhu(beizhu.getText().toString());
		}
		if(conModel.getDatatime().equals("")){
			conModel.setDatatime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		}
		conModel.setDizhi(shengfen.getText().toString()+"-"+chengshi.getText().toString()+"-"+quxian.getText().toString());
		conModel.setKehushuxing(edtKehushuxing.getText().toString());
		conModel.setKehuzu(edtYonghuzu.getText().toString());
		conModel.setRenzhiqudao(edtRenzhiqudao.getText().toString());
		if(imgMan.isChecked()){
			conModel.setXingbie("男");
		}else
			conModel.setXingbie("女");
		conModel.setZijinshili(edtZijinshili.getText().toString());
		conModel.setYixiangyetai(edtYixiangyetai.getText().toString());
		conModel.setYixiangmianji(edtYixiangmianji.getText().toString());
		conModel.setYixianghuxing(edtYixianghuxing.getText().toString());
		conModel.setYixiang(edtYixiang.getText().toString());
		conModel.setYisuan(edtYisuan.getText().toString());
		conModel.setQuyu(quyu.getText().toString());
		conModel.setLouhao(louhao.getText().toString());
		conModel.setShenfenzheng(shenfenzheng.getText().toString());
		if(chengjiaojine.isEnabled())
			conModel.setChengjiaojine(chengjiaojine.getText().toString());
		else
			conModel.setChengjiaojine("");
		if(!laoyezhuphone.equals("") && !chengjiaojine.getText().toString().equals("") && chengjiaojine.isEnabled()){
			conModel.setLaoyezhuphone(laoyezhuphone);
		}else{
			conModel.setLaoyezhuphone("");
		}
		
		
		
		return true;
	}
	
	/**
	 * 发音人选择。
	 */
	private void showPresonSelectDialog() {
		ApkInstaller mInstaller = new  ApkInstaller(Register.this); 
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			}else {
				SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);				
			}
	}
	
	 @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (resultCode == RESULT_OK) {  
	            Uri uri = data.getData();  
	            Log.e("uri", uri.toString());  
	            ContentResolver cr = this.getContentResolver();  
	            try {  
	            	if(!bmp.isRecycled()){
	            		bmp.recycle();
	            	}
	            	bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));  
	            } catch (FileNotFoundException e) {  
	                Log.e("Exception", e.getMessage(),e);  
	            }  
	        }  
	        super.onActivityResult(requestCode, resultCode, data);  
	    }  
	
	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	private void setTsParam(SpeechSynthesizer mTts){
		//设置合成
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD))
		{
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			//设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME,voicer);
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			//设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME,"");
		}
		
		//设置语速
		mTts.setParameter(SpeechConstant.SPEED,mSharedPreferences.getString("speed_preference", "50"));

		//设置音调
		mTts.setParameter(SpeechConstant.PITCH,mSharedPreferences.getString("pitch_preference", "50"));

		//设置音量
		mTts.setParameter(SpeechConstant.VOLUME,mSharedPreferences.getString("volume_preference", "50"));
		
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,mSharedPreferences.getString("stream_preference", "3"));
	}
	
	@Override
	protected void onDestroy() {
		if(bmp != null && !bmp.isRecycled()){
			bmp.recycle();
		}
		
		PhoneReceiver.dimissLoad();
		
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
		super.onDestroy();
	}
	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){

		@Override
		public void onBeginOfSpeech() {	
			showTip("开始说话");
		}


		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}



		@Override
		public void onResult(RecognizerResult results, boolean isLast) {		
			String text = JsonParser.parseIatResult(results.getResultString());
			beizhu.append(text);
			beizhu.setSelection(beizhu.length());
			if(isLast) {
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}


		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener(){
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			beizhu.append(text);
			beizhu.setSelection(beizhu.length());
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
	}
}

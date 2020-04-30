<template>
	<view>
		<image class="top-img" src="/static/img/main_home_top.png" mode="widthFix"></image>
		<view style="color: #FFFFFF;margin-top: -400upx;margin-left: 240upx;">环保监测云平台</view>
		<view class="weather">
			<view>
				<text>{{wendu}}</text>
				<image :src="weatherImg" style="width: 60upx; height: 60upx; margin-left: 10upx;"></image>
			</view>
			<view>{{type}}&nbsp;{{fengxiang}}{{fengli}}</view>
		</view>
		<view class="header">
			<view class="order">
				<view class="balance-info">
					<view class="left" @click="produceMonitor()">
						<view class="box" style="margin-left: -20upx;">
							<view class="num">{{companyNum}}家</view>
							<view class="text">企业数量</view>
						</view>
						<view class="box">
							<view class="num">{{governNum}}处</view>
							<view class="text">治污设施</view>
						</view>
						<view class="box">
							<view class="num">{{produceNum}}处</view>
							<view class="text">监测产污设施</view>
						</view>
						<view class="box">
							<view class="num">{{monitorNum}}个</view>
							<view class="text">监测点数量</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="orders1">
			<view class="exception-left">
				<view class="title">治污设施异常情况</view>
				<view class="yesterday-info">
					<view class="yes-mark">昨日</view>
					<view class="exception-info" @click="governExceptionYesterday()">
						<view class="info-progress">
							<view class="progress-des">
								<view class="company-name">治污设施异常企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumGovernExceptionYesterday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width1}"></view>
							</view>
						</view>
						<view class="info-progress2">
							<view class="progress-des">
								<view class="company-name">涉及治污设施</view>
								<view class="company-count"><span class="c-count">{{ governNumInvolveYesterday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width3}"></view>
							</view>
						</view>
					</view>
				</view>
				<view class="today-info">
					<view class="yes-mark">今日</view>
					<view class="exception-info" @click="governExceptionToday()">
						<view class="info-progress">
							<view class="progress-des">
								<view class="company-name">治污设施异常企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumGovernExceptionToday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width5}"></view>
							</view>
						</view>
						<view class="info-progress2">
							<view class="progress-des">
								<view class="company-name">涉及治污设施</view>
								<view class="company-count"><span class="c-count">{{ governNumInvolveToday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width7}"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="exception-right">
				<view class="title">限产/停产执行异常</view>
				<view class="yesterday-info">
					<view class="exception-info">
						<view class="info-progress">
							<view class="progress-des">
								<view class="company-name">执行停产限产企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumStopLimitProduceYesterday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width2}"></view>
							</view>
						</view>
						<view class="info-progress2">
							<view class="progress-des">
								<view class="company-name">未按要求执行企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumNotAsRequiredYesterday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width4}"></view>
							</view>
						</view>
					</view>
				</view>
				<view class="today-info">
					<view class="exception-info">
						<view class="info-progress">
							<view class="progress-des">
								<view class="company-name">执行停产限产企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumStopLimitProduceToday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width6}"></view>
							</view>
						</view>
						<view class="info-progress2">
							<view class="progress-des">
								<view class="company-name">未按要求执行企业</view>
								<view class="company-count"><span class="c-count">{{ companyNumNotAsRequiredToday }}</span>家</view>
							</view>
							<view class="cu-progress">
								<view class="bg-blue" :style="{width: width8}"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="ucharts-table">
			<view class="ucharts-tab">
				<view :class="showAQIOrEleUcharts ? 'aqi-compare active' : 'aqi-compare'" @click="chageAQITab()">AQI/生产用电对比</view>
				<view :class="!showAQIOrEleUcharts ? 'useEle active' : 'useEle'" @click="chageEleTab()">用电量</view>
			</view>
			<view class="qiun-charts" v-show="showAQIOrEleUcharts">
				<image src="/static/img/no_data.png" v-if="imgLineAFlag"></image>
				<canvas v-if="lineAFlag" canvas-id="canvasLineA" id="canvasLineA" class="charts" disable-scroll=true @touchstart="touchLineA" @touchmove="moveLineA" @touchend="touchEndLineA"></canvas>
			</view>
			<view class="qiun-charts" v-show="!showAQIOrEleUcharts">
				<image src="/static/img/no_data.png" v-if="imgColumnFlag"></image>
				<canvas v-if="columnFlag" canvas-id="canvasColumn" id="canvasColumn" class="charts" disable-scroll=true @touchstart="touchColumn" @touchmove="moveColumn" @touchend="touchEndColumn"></canvas>
			</view>
		</view>
		<view class="ucharts-table2">
			<view class="ucharts-tab">
				<view :class="showExceptionUcharts ? 'aqi-compare active' : 'aqi-compare'" @click="chageIndustryTab()">各行业异常企业占比</view>
				<view :class="!showExceptionUcharts ? 'useEle active' : 'useEle'" @click="chageAreaTab()">各区域异常企业占比</view>
			</view>
			<view class="qiun-charts" v-show="showExceptionUcharts">
				<image src="/static/img/no_data.png" v-if="imgPieIndustryFlag"></image>
				<canvas v-if="pieIndustryFlag" canvas-id="canvasPieIndustry" id="canvasPieIndustry" class="charts" @touchstart="touchPieIndustry"></canvas>
			</view>
			<view class="qiun-charts" v-show="!showExceptionUcharts">
				<image src="/static/img/no_data.png" v-if="imgPieAreaFlag"></image>
				<canvas v-if="pieAreaFlag" canvas-id="canvasPieArea" id="canvasPieArea" class="charts" @touchstart="touchPieArea"></canvas>
			</view>
		</view>
	</view>
</template>
<script>
	import uCharts from '@/components/u-charts/u-charts.js';
	var _self;
	var canvaLineA = null;
	var canvaColumn = null;
	var canvaPie = null;
	let hasLogin;
	export default {
		data() {
			return {
				userId: '',//用户id
				wendu: '',//天气温度
				weatherImg: '',//天气图片
				type: '',//天气类型
				fengxiang: '',//风向
				fengli: '',//风力
				companyNum: 0,//企业家数
				governNum: 0,//治污设施
				produceNum: 0,//监测产污设施
				monitorNum: 0,//监测点数量
				companyNumGovernExceptionYesterday: 0,//治污设施异常企业昨日
				companyNumStopLimitProduceYesterday: 0,//执行停产限产企业昨日
				governNumInvolveYesterday: 0,//涉及治污设施昨日
				companyNumNotAsRequiredYesterday: 0,//未按要求执行企业昨日
				companyNumGovernExceptionToday: 0,//治污设施异常企业今日
				companyNumStopLimitProduceToday: 0,//执行停产限产企业今日
				governNumInvolveToday: 0,//涉及治污设施今日
				companyNumNotAsRequiredToday: 0,//未按要求执行企业今日
				width1: '0%',
				width2: '0%',
				width3: '0%',
				width4: '0%',
				width5: '0%',
				width6: '0%',
				width7: '0%',
				width8: '0%',
				//图表相关
				cWidth: '',
				cHeight: '',
				pixelRatio: 1,
				imgLineAFlag: false,
				lineAFlag: true,
				imgColumnFlag: false,
				columnFlag: true,
				imgPieIndustryFlag: false,
				pieIndustryFlag: true,
				imgPieAreaFlag: false,
				pieAreaFlag: true,
				showAQIOrEleUcharts: true,
				showExceptionUcharts: true,
				chartData: {
					categories: ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16'],
					series: [{
						name: '生产用电',
						data: [35.89, 18.95, 26.34, 37.06, 18.3, 31.04, 55.41, 64.04, 68.51, 69.88, 96.26, 41.89, 60.22, 65.21, 63.4, 61.15, 59.48],
						color: '#000000'
					}, {
						name: 'AQI',
						data: [29, 26, 30, 35, 30, 65, 38, 62, 73, 72, 65, 65, 63, 60, 60, 56, 56]
					}]
				},
				column: '',
				industry: '',
				area: '',
				today: '',
				yesterday: ''
			};
		},
		//页面右上角企业
		onNavigationBarButtonTap(e) {
		    uni.reLaunch({
				url: '/pages/company/company'
		    });
		},
		onLoad() {
			_self = this;
			// #ifdef H5
			hasLogin = localStorage.getItem("hasLogin");
			//#endif
			// #ifdef APP-PLUS
			hasLogin = plus.storage.getItem("hasLogin");
			// #endif
			//判断是否登录
			if(!hasLogin){
				uni.reLaunch({
					url: '/pages/login/login'
				});
			}
			this.cWidth = uni.upx2px(750);
			this.cHeight = uni.upx2px(500);
			//获取当前年-月-日
			let year1 = new Date().getFullYear();
			let month1 = new Date().getMonth() + 1 < 10 ? "0" + (new Date().getMonth() + 1) : new Date().getMonth() + 1;
			let day1 = new Date().getDate() < 10 ? "0" + new Date().getDate() : new Date().getDate();
			this.today = year1 + '-' + month1 + '-' + day1;
			//获取昨天年-月-日
			var time = (new Date).getTime() - 24*60*60*1000;
			var date = new Date(time);
			var year2 = date.getFullYear();
			var month2 = date.getMonth() > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
			var day2 = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
			this.yesterday = year2 + '-' + month2 + '-' + day2;
			//获取首页展示信息
			this.getHomeInfo();
			//获取天气信息
			uni.request({
				url:'http://wthrcdn.etouch.cn/weather_mini?city=德州',
				data:{},
				success: (res) => {
					var low = res.data.data.forecast[0].low;
					var high = res.data.data.forecast[0].high;
					this.wendu = low.split(' ')[1] + '~' + high.split(' ')[1];
					this.type = res.data.data.forecast[0].type;
					if(this.type == '多云'){
						this.weatherImg = '/static/img/cloudy.png';
					}else if(this.type == '晴'){
						this.weatherImg = '/static/img/fine.png';
					}else if(this.type == '阴'){
						this.weatherImg = '/static/img/yin.png';
					}
					this.fengxiang = res.data.data.forecast[0].fengxiang;
					this.fengli = ((res.data.data.forecast[0].fengli).split('[')[2]).split(']')[0];
				}
			})
		},
		// 下拉刷新
		onPullDownRefresh() {
			_self.imgLineAFlag = false;
			_self.lineAFlag = true;
			_self.imgColumnFlag = false;
			_self.columnFlag = true;
			_self.imgPieIndustryFlag = false;
			_self.pieIndustryFlag = true;
			_self.imgPieAreaFlag = false;
			_self.pieAreaFlag = true;
			this.getHomeInfo();
			setTimeout(function () {
				uni.stopPullDownRefresh();
			}, 1000);
		},
		methods: {
			//获取首页展示信息
			getHomeInfo(){
				uni.request({
					method: 'GET',
					url: this.baseUrl+'/electric/appHome/getHomeInfo.do',
					success: (res) => {
						if(res.statusCode == 200){
							if(res.data.msg == 'success'){
								// #ifdef H5
								localStorage.setItem("companyNumMax", JSON.stringify(res.data.data.companyNum));
								//#endif
								// #ifdef APP-PLUS
								plus.storage.setItem("companyNumMax", JSON.stringify(res.data.data.companyNum));
								//#endif
								this.companyNum = res.data.data.companyNum;
								this.governNum = res.data.data.governNum;
								this.produceNum = res.data.data.produceNum;
								this.monitorNum = res.data.data.monitorNum;
								this.companyNumGovernExceptionYesterday = res.data.data.companyNumGovernExceptionYesterday;
								if(0 != res.data.data.companyNumGovernExceptionYesterday){
									this.width1 = Math.round((res.data.data.companyNumGovernExceptionYesterday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								this.companyNumStopLimitProduceYesterday = res.data.data.companyNumStopLimitProduceYesterday;
								if(0 != res.data.data.companyNumStopLimitProduceYesterday){
									this.width2 = Math.round((res.data.data.companyNumStopLimitProduceYesterday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								this.governNumInvolveYesterday = res.data.data.governNumInvolveYesterday;
								if(0 != res.data.data.governNumInvolveYesterday){
									this.width3 = Math.round((res.data.data.governNumInvolveYesterday) / (res.data.data.governNum) * 100 * 100) / 100 + '%';
								}
								this.companyNumNotAsRequiredYesterday = res.data.data.companyNumNotAsRequiredYesterday;
								if(0 != res.data.data.companyNumNotAsRequiredYesterday){
									this.width4 = Math.round((res.data.data.companyNumNotAsRequiredYesterday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								this.companyNumGovernExceptionToday = res.data.data.companyNumGovernExceptionToday;
								if(0 != res.data.data.companyNumGovernExceptionToday){
									this.width5 = Math.round((res.data.data.companyNumGovernExceptionToday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								this.companyNumStopLimitProduceToday = res.data.data.companyNumStopLimitProduceToday;
								if(0 != res.data.data.companyNumStopLimitProduceToday){
									this.width6 = Math.round((res.data.data.companyNumStopLimitProduceToday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								this.governNumInvolveToday = res.data.data.governNumInvolveToday;
								if(0 != res.data.data.governNumInvolveToday){
									this.width7 = Math.round((res.data.data.governNumInvolveToday) / (res.data.data.governNum) * 100 * 100) / 100 + '%';
								}
								this.companyNumNotAsRequiredToday = res.data.data.companyNumNotAsRequiredToday;
								if(0 != res.data.data.companyNumNotAsRequiredToday){
									this.width8 = Math.round((res.data.data.companyNumNotAsRequiredToday) / (res.data.data.companyNum) * 100 * 100) / 100 + '%';
								}
								//AQI/错峰生产对比分析
								_self.showLineA("canvasLineA", _self.chartData);
								//用电量
								let Column = {categories: [], series: [{'name': '今日', 'data': []},{'name': '昨日', 'data': []}]};
								if(res.data.data.elecListToday.length > 0 && res.data.data.elecListYesterday.length > 0){
									for(let i = 0; i < res.data.data.elecListToday.length; i++){
										Column.categories[i] = (res.data.data.elecListToday[i].hours.split(' ')[1]).substr(0,2);
										Column.series[0].data[i] = res.data.data.elecListToday[i].enavgs;
									}
									for(let i = 0; i < res.data.data.elecListYesterday.length; i++){
										Column.series[1].data[i] = res.data.data.elecListYesterday[i].enavgs;
									}
									_self.column = Column;
									_self.showColumn("canvasColumn", Column);
								}else{
									_self.column = Column;
									_self.imgColumnFlag = true;
									_self.columnFlag = false;
								}
								//各行业异常企业占比
								if(res.data.data.industryList.length > 0){
									let PieIndustry = {series:[]};
									for(let i = 0; i < res.data.data.industryList.length; i++){
										var data = {'name': res.data.data.industryList[i].type, 'data': res.data.data.industryList[i].companyNum};
										PieIndustry.series[i] = data;
									}
									_self.industry = PieIndustry;
									_self.showPieIndustry("canvasPieIndustry", PieIndustry);
								}else{
									_self.imgPieIndustryFlag = true;
									_self.pieIndustryFlag = false;
								}
								//各区域异常企业占比
								if(res.data.data.areaList.length > 0){
									let PieArea = {series:[]};
									for(let i = 0; i < res.data.data.areaList.length; i++){
										var data = {'name': res.data.data.areaList[i].type, 'data': res.data.data.areaList[i].companyNum};
										PieArea.series[i] = data;
									}
									_self.area = PieArea;
									_self.showPieArea("canvasPieArea", PieArea);
								}else{
									_self.imgPieAreaFlag = true;
									_self.pieAreaFlag = false;
								}
							}else{
								uni.showToast({
									icon: 'none',
									title: '网络异常请重试'
								})
							}
						}else{
							uni.showToast({
								icon: 'none',
								title: '网络异常请重试'
							})
						}
					},
					fail: (res) => {
						uni.showToast({
							icon: 'none',
							title: '网络异常请重试'
						})
					}
				})
			},
			//产污监控
			produceMonitor(){
				uni.switchTab({
					url: '../monitor/monitor'
				})
			},
			//治污设施异常昨日
			governExceptionYesterday(){
				// #ifdef H5
				localStorage.setItem('date', this.yesterday)
				localStorage.setItem('warnCode', '5')
				// #endif
				// #ifdef APP-PLUS
				plus.storage.setItem('date', this.yesterday)
				plus.storage.setItem('warnCode', '5')
				// #endif
				uni.switchTab({
					url: '../warn/warn'
				})
			},
			//治污设施异常今日
			governExceptionToday(){
				// #ifdef H5
				localStorage.setItem('date', this.today)
				localStorage.setItem('warnCode', '5')
				// #endif
				// #ifdef APP-PLUS
				plus.storage.setItem('date', this.today)
				plus.storage.setItem('warnCode', '5')
				// #endif
				uni.switchTab({
					url: '../warn/warn'
				})
			},
			//AQI/错峰生产对比分析按钮改变事件
			chageAQITab(){
				this.showAQIOrEleUcharts = true;
				_self.showLineA("canvasLineA", _self.chartData);
			},
			//AQI/错峰生产对比分析
			showLineA(canvasId, chartData){
				canvaLineA = new uCharts({
					$this: _self,
					canvasId: canvasId,
					type: 'line',
					fontSize: 11,
					legend: {show: true},
					dataLabel: false,
					dataPointShape: true,
					background: '#FFFFFF',
					pixelRatio: _self.pixelRatio,
					categories: chartData.categories,
					series: chartData.series,
					animation: true,
					enableScroll: true,//开启图表拖拽功能
					xAxis: {
						type: 'grid',
						gridType: 'dash',
						itemCount: 10,//x轴单屏显示数据的数量，默认为5个
						scrollShow: true,//新增是否显示滚动条，默认false
						scrollAlign: 'left',//滚动条初始位置
						scrollBackgroundColor: '#F7F7FF',//默认为 #EFEBEF
						scrollColor: '#DEE7F7',//默认为 #A6A6A6
					},
					yAxis: {
						gridType: 'dash',
						min: 0,
						format:(val)=>{return val.toFixed(0)}
					},
					width: _self.cWidth*_self.pixelRatio,
					height: _self.cHeight*_self.pixelRatio,
					extra: {
						line:{
							type: 'straight'
						}
					}
				});
			},
			touchLineA(e) {
				canvaLineA.scrollStart(e);
				canvaLineA.showToolTip(e, {
					format: function (item, category) {
						return category + ' ' + item.name + ':' + item.data 
					}
				});
			},
			moveLineA(e) {
				canvaLineA.scroll(e);
			},
			touchEndLineA(e) {
				canvaLineA.scrollEnd(e);
				//下面是toolTip事件，如果滚动后不需要显示，可不填写
				canvaLineA.showToolTip(e, {
					format: function (item, category) {
						return category + ' ' + item.name + ':' + item.data 
					}
				});
			},
			//用电量按钮改变事件
			chageEleTab(){
				this.showAQIOrEleUcharts = false;
				_self.showColumn("canvasColumn", _self.column);
			},
			//用电量
			showColumn(canvasId, chartData){
				canvaColumn = new uCharts({
					$this: _self,
					canvasId: canvasId,
					type: 'column',
					legend: {show: true},
					dataLabel: false,
					dataPointShape: true,
					fontSize: 11,
					background: '#FFFFFF',
					pixelRatio: _self.pixelRatio,
					animation: true,
					categories: chartData.categories,
					series: chartData.series,
					xAxis: {
						type: 'grid',
						gridType: 'dash',
						itemCount: 10,//x轴单屏显示数据的数量，默认为5个
						scrollShow: true,//新增是否显示滚动条，默认false
						scrollAlign: 'left',//滚动条初始位置
						scrollBackgroundColor: '#F7F7FF',//默认为 #EFEBEF
						scrollColor: '#DEE7F7',//默认为 #A6A6A6
					},
					yAxis: {
						gridType: 'dash',
						min: 0,
						format:(val)=>{return val.toFixed(0)}
					},
					width: _self.cWidth*_self.pixelRatio,
					height: _self.cHeight*_self.pixelRatio,
					extra: {
						lineStyle: 'straight',
						column: {
							type: 'group',
							width: _self.cWidth*_self.pixelRatio*0.45/chartData.categories.length
						}
					}
				});
			},
			touchColumn(e){
				canvaColumn.scrollStart(e);
				canvaColumn.showToolTip(e, {
					format: function (item, category) {
						if(typeof item.data === 'object'){
							return category + ' ' + item.name + ':' + item.data.value 
						}else{
							return category + ' ' + item.name + ':' + item.data 
						}
					}
				});
			},
			moveColumn(e) {
				canvaColumn.scroll(e);
			},
			touchEndColumn(e) {
				canvaColumn.scrollEnd(e);
				//下面是toolTip事件，如果滚动后不需要显示，可不填写
				canvaColumn.showToolTip(e, {
					format: function (item, category) {
						return category + ' ' + item.name + ':' + item.data 
					}
				});
			},
			//各行业异常企业占比按钮改变事件
			chageIndustryTab(){
				this.showExceptionUcharts = true;
				_self.showPieIndustry("canvasPieIndustry", _self.industry);
			},
			//各行业异常企业占比
			showPieIndustry(canvasId, chartData){
				canvaPie = new uCharts({
					$this: _self,
					canvasId: canvasId,
					type: 'pie',
					fontSize: 11,
					legend: {show: true},
					background: '#FFFFFF',
					pixelRatio: _self.pixelRatio,
					series: chartData.series,
					animation: true,
					width: _self.cWidth*_self.pixelRatio,
					height: _self.cHeight*_self.pixelRatio,
					dataLabel: true,
					extra: {
						pie: {
						  lableWidth: 15
						}
					},
				});
			},
			touchPieIndustry(e){
				canvaPie.showToolTip(e, {
					format: function (item) {
						return item.name + ':' + item.data 
					}
				});
			},
			//各区域异常企业占比按钮改变事件
			chageAreaTab(){
				this.showExceptionUcharts = false;
				_self.showPieArea("canvasPieArea", _self.area);
			},
			//各区域异常企业占比
			showPieArea(canvasId, chartData){
				canvaPie = new uCharts({
					$this: _self,
					canvasId: canvasId,
					type: 'pie',
					fontSize: 11,
					legend: {show: true},
					background: '#FFFFFF',
					pixelRatio: _self.pixelRatio,
					series: chartData.series,
					animation: true,
					width: _self.cWidth*_self.pixelRatio,
					height: _self.cHeight*_self.pixelRatio,
					dataLabel: true,
					extra: {
						pie: {
						  lableWidth: 15
						}
					},
				});
			},
			touchPieArea(e){
				canvaPie.showToolTip(e, {
					format: function (item) {
						return item.name + ':' + item.data 
					}
				});
			},
		},
	}
</script>
<style lang="scss">
	.weather{
		font-size: 22upx;
		margin-top: -20upx;
		margin-left: 520upx;
	}
	.top-img {
		width: 100%;
		height: 100%;
	}
	page{background-color:#F8F8FF}
	.header{
		background-color: #0faeff;
		width: 100%;
		height: 30vw;
		padding: 0 4%;
		display: flex;
		align-items: center;
		margin-left: -30upx;
		margin-top: -20upx;
	}
	.order{
		width: 100%;
		margin-top: -100upx;
		// background-color: #0faeff;
		.balance-info{
			display: flex;
			padding: 10upx 0;
			.left{
				width: 100%;
				display: flex;
				.box{
					width: 50%;
					font-size: 28upx;
					font-weight: 600;
					display: flex;
					flex-direction:column ;
					.num{
						width: 100%;
						height: 50upx;
						display: flex;
						justify-content: center;
						align-items: flex-end;
						color: #FFFFFF;
					}
					.text{
						width: 100%;
						display: flex;
						justify-content: center;
						color: #FFFFFF;
						font-size: 28upx;
					}
				}
			}
		}
	}
	.cu-progress {
		overflow: hidden;
		height: 12rpx;
		background-color: #ebeef5;
		// display: inline-flex;
		align-items: center;
		width: 100%;
	}
	.cu-progress+view,
	.cu-progress+text {
		line-height: 1;
	}
	.cu-progress.xs {
		height: 10upx;
	}
	.cu-progress.sm {
		height: 20upx;
	}
	.cu-progress view {
		width: 0;
		height: 100%;
		align-items: center;
		display: flex;
		justify-items: flex-end;
		justify-content: space-around;
		font-size: 20upx;
		color: #ffffff;
		transition: width 0.6s ease;
	}
	.cu-progress text {
		align-items: center;
		display: flex;
		font-size: 20upx;
		color: #333333;
		text-indent: 10upx;
	}
	.cu-progress.text-progress {
		padding-right: 60upx;
	}
	.cu-progress.striped view {
		background-image: linear-gradient(45deg, rgba(255, 255, 255, 0.15) 25%, transparent 25%, transparent 50%, rgba(255, 255, 255, 0.15) 50%, rgba(255, 255, 255, 0.15) 75%, transparent 75%, transparent);
		background-size: 72upx 72upx;
	}
	.cu-progress.active view {
		animation: progress-stripes 2s linear infinite;
	}
	@keyframes progress-stripes {
		from {
			background-position: 72upx 0;
		}
		to {
			background-position: 0 0;
		}
	}
	.bg-red {
		background-color: #e54d42;
		color: #ffffff;
	}
	.bg-blue {
		background-color: #0081ff;
		color: #ffffff;
	}
	.orders1 {
		position: relative;
		top: -56upx;
		display: flex;
		width: 95%;
		margin: -40rpx auto calc(11vw + 60upx);
		// border: 1px solid red;
		box-shadow: 0 0 16rpx rgba(0,0,0,0.15);
		border-radius: 20rpx;
		background-color: #FFFFFF;
		font-size: 28rpx;
		padding-bottom: 16upx;
		.exception-left {
			width: 360rpx;
			// border: 1px solid #007AFF;
			.title {
				padding: 40rpx 20rpx 20rpx;
				margin-left: 50rpx;
				font-size: 32rpx;
			}
			.yesterday-info,.today-info {
				display: flex;
				.yes-mark {
					position: relative;
					width: 32rpx;
					height: 90rpx;
					background-color: #0faeff;
					color: #fff;
					padding: 30rpx 4rpx 16rpx;
					margin-right: 20rpx;
				}
				.exception-info {
					.progress-des {
						display: flex;
						justify-content: space-between;
						margin-top: 10rpx;
						.company-count {
							margin-left: 10rpx;
							color: #4169E1;
							.c-count {
								display: inline-block;
								font-size: 34rpx;
								margin-top: -6rpx;
								margin-right: 2rpx;
							}
						}
					}
					.info-progress2 {
						margin-top: 24rpx;
					}
				}
			}
			.today-info {
				margin-top: 40rpx;
				.yes-mark {
					background-color: #00CD00;
				}
				.exception-info {
					.progress-des {
						.company-count {
							color: #00CD00;
						}
					}
					.cu-progress {
						.bg-blue {
							background-color: #32CD32;
						}
					}
				}
			}
		}
		.exception-right {
			margin-left: 32rpx;
			.title {
				padding: 40rpx 10rpx 20rpx;
				// margin-left: 50rpx;
				font-size: 32rpx;
			}
			.yesterday-info,.today-info {
				display: flex;
				.exception-info {
					.progress-des {
						display: flex;
						justify-content: space-between;
						margin-top: 10rpx;
						.company-count {
							margin-left: 10rpx;
						}
						.company-count {
							color: #4169E1;
							.c-count {
								display: inline-block;
								font-size: 34rpx;
								margin-top: -6rpx;
								margin-right: 2rpx;
							}
						}
					}
					.info-progress2 {
						margin-top: 24rpx;
					}
				}
			}
			.today-info {
				margin-top: 40rpx;
				.exception-info {
					.progress-des {
						.company-count {
							color: #00CD00;
						}
					}
					.cu-progress {
						.bg-blue {
							background-color: #32CD32;
						}
					}
				}
			}
		}
	}
	.ucharts-table,.ucharts-table2{
		width: 100%;
		margin: -152rpx auto 0;
		background-color: #fff;
		padding-top: 20rpx;
		.ucharts-tab {
			width:640rpx;
			margin: 0 auto;
			outline: 0;
			display: flex;
			font-size: 28rpx;
			.aqi-compare,.useEle {
				border: 1px solid #00B2EE;
				// box-shadow: 0 0 1px #00B2EE;
				// border-radius: 10rpx;
				outline: 0;
				width: 305rpx;
				height: 40rpx;
				line-height: 40rpx;
				text-align: center;
				padding: 10rpx 14rpx;
				color: #1C86EE;
				font-weight: bold;
			}	
			.aqi-compare {
				border-right: 0;
				border-top-left-radius: 10rpx;
				border-bottom-left-radius: 10rpx ;
			}
			.useEle {
				border-top-right-radius: 10rpx;
				border-bottom-right-radius: 10rpx ;
			}
			.active {
				background-color: #1C86EE;
				color: #fff;
				font-weight: normal;
			}
		}
	}
	.ucharts-table2{
		margin: 40rpx auto;
	}
	page{background:#F2F2F2;width: 750upx;overflow-x: hidden;}
	.qiun-padding{padding:2%; width:100%;}
	.qiun-wrap{display:flex; flex-wrap:wrap;}
	.qiun-rows{display:flex; flex-direction:row !important;}
	.qiun-columns{display:flex; flex-direction:column !important;}
	.qiun-common-mt{margin-top:10upx;}
	.qiun-bg-white{background:#FFFFFF;}
	.qiun-title-bar{width:100%; padding:10upx 2%; flex-wrap:nowrap;}
	.qiun-title-dot-light{border-left: 10upx solid #0ea391; padding-left: 10upx; font-size: 32upx;color: #000000}
	.qiun-charts{width: 750upx; height:500upx;background-color: #FFFFFF;}
	.charts{width: 750upx; height:500upx;background-color: #FFFFFF;}
</style>